package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;

public class NameNode extends NodeImpl implements Expression, LValue {
	public String primaryName;
	public ArrayList<GenericNode> generics;
	
    public NameNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		//System.out.print("Replace: " + primaryName);
		if (primaryName != null)
			primaryName = c.getFullName(primaryName);
		// don't have to check if one of primaryName or bounds is set, as this is handled by the parser
		
		// resolve the nested structures as well
		if (generics != null) {
			for (GenericNode n : generics) {
				n.resolveImports(c);
			}
		}
		
		//System.out.println(" -> " + primaryName);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		if (generics != null) {
			throw new CompileException("Generics compiling not supported yet.", getFileName(), getLine());
			// TODO
		}
		
		if (!primaryName.contains(".")) {
			// on the right side, get the result
			int tableLookup = s.lookup(primaryName);

			if (tableLookup == -1) {
				throw new CompileException("symbol: '" + primaryName + "' not found.",
						getFileName(), getLine());
			}

			final Types type = s.getType(primaryName);

			if (tableLookup == SymbolTable.local) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetLocalStatement(result, primaryName, getFileName(), getLine()));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetParamStatement(result, primaryName, getFileName(), getLine()));
			}  else if (tableLookup == SymbolTable.staticFields) {
				// static field
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetStaticFieldStatement(f.parentClass, primaryName, result, getFileName(), getLine()));
			} else if (tableLookup == SymbolTable.instanceFields) {
				// load 'this' pointer
				Register thisPointer = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetParamStatement(thisPointer, "this", getFileName(), getLine()));
				
				// load the field from 'this' pointer
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetInstanceFieldStatement(thisPointer, primaryName, result,
						getFileName(), getLine()));
				
				f.history.setName(primaryName);
			}
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character
			int tableLookup = s.lookup(split[0]);
			if (tableLookup == SymbolTable.className) {
				// get the static field, then do the chain of instance fields
				Register result = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetStaticFieldStatement(split[0], split[1], result, getFileName(), getLine()));
				// store these two names last used
				f.history.setName(split[0]);
				f.history.setName(split[1]);
				for (int i = 2; i < split.length; i++) {
					Register temp3 = f.allocator.getNext(Types.UNKNOWN);
					f.history.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3,
							getFileName(), getLine()));
					result = temp3;
				}
				
			} else {
				// instance fields of the parameter
				NameNode temp = new NameNode(getFileName(), getLine());
				temp.primaryName = split[0];
				
				temp.compile(s, f);
				Register result = f.allocator.getLast();
				
				for (int i = 1; i < split.length; i++) {
					Register temp3 = f.allocator.getNext(Types.UNKNOWN);
					f.history.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3,
							getFileName(), getLine()));
					
					result = temp3;
				}
			}
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f) throws CompileException {
		
		if (generics != null) {
			throw new CompileException("Generics compiling not supported yet.", getFileName(), getLine());
			// TODO
		}
		
		f.history.setName(primaryName);
		// on the left side, get the address
		if (!primaryName.contains(".")) {
			int tableLookup = s.lookup(primaryName);
			if (tableLookup == -1) {
				throw new CompileException("symbol: '" + primaryName + "' not defined.",
						getFileName(), getLine());
			}
			final Types type = s.getType(primaryName);
			 
			if (tableLookup == SymbolTable.local) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetLocalAddressStatement(result, primaryName, getFileName(), getLine()));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetParamAddressStatement(result, primaryName, getFileName(), getLine()));
			} else if (tableLookup == SymbolTable.staticFields) {
				// static field
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetStaticFieldAddressStatement(f.parentClass, primaryName, result, getFileName(), getLine()));
			} else if (tableLookup == SymbolTable.instanceFields) {
				// instance field: load 'this' pointer
				Register thisPointer = f.allocator.getNext(Types.UNKNOWN);
				f.statements.add(new GetParamStatement(thisPointer, "this", getFileName(), getLine()));
				
				// load the field from 'this' pointer
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetInstanceFieldAddressStatement(thisPointer, primaryName, result,
						getFileName(), getLine()));
			} else {
				throw new CompileException("unsure what to do here", "NameNode.java", 152);
			}
			// don't do anything
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character

			// construct a primaryExpressionNode
			PrimaryExpressionNode ex = new PrimaryExpressionNode(getFileName(), getLine());

			// set the name - use NoOp since you need something as the prefix.
			f.history.setName(split[0]);
			ex.prefix = new NoOp(getFileName(), getLine());
			ex.suffixes = new ArrayList<>();
			// the rest are consecutive fieldAccesses
			for (int i = 1; i < split.length; i++) {
				FieldExpressionNode field = new FieldExpressionNode(getFileName(), getLine());
				field.identifier = split[i];
				ex.suffixes.add(field);
			}
			
			// compile address the primaryExpressionNode
			ex.compileAddress(s, f);
		}
	}
}
