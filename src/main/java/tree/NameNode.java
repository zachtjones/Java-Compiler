package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetInstanceFieldAddressStatement;
import intermediate.GetInstanceFieldStatement;
import intermediate.GetLocalAddressStatement;
import intermediate.GetLocalStatement;
import intermediate.GetParamAddressStatement;
import intermediate.GetParamStatement;
import intermediate.GetStaticFieldStatement;
import intermediate.InterFunction;
import intermediate.Register;

public class NameNode implements Node, Expression, LValue {
	public String primaryName;
	public ArrayList<GenericNode> generics;
	
	public String fileName;
    public int line;
    
    public NameNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
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
			throw new CompileException("Generics compiling not supported yet.", fileName, line);
			// TODO
		}
		
		System.out.println(primaryName); //TODO
		if (!primaryName.contains(".")) {
			// on the right side, get the result
			int tableLookup = s.lookup(primaryName);
			String type;
			if (tableLookup == -1) {
				throw new CompileException("symbol: '" + primaryName + "' not found.",
						fileName, line);
			} else {
				type = s.getType(primaryName);
			}
			if (tableLookup == SymbolTable.local) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetLocalStatement(result, primaryName, fileName, line));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetParamStatement(result, primaryName, fileName, line));
			} else { // assume static/instance field / method
				// load 'this' pointer
				Register thisPointer = f.allocator.getNext(Register.REFERENCE);
				f.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
				
				// load the field from 'this' pointer
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetInstanceFieldStatement(thisPointer, primaryName, result,
						fileName, line));
				
				f.history.setName(primaryName);
			}
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character
			int tableLookup = s.lookup(split[0]);
			if (tableLookup == SymbolTable.className) {
				// get the static field, then do the chain of instance fields
				Register result = f.allocator.getNext(Register.REFERENCE);
				f.statements.add(new GetStaticFieldStatement(split[0], split[1], result, fileName, line));
				f.history.setName(split[1]);
				for (int i = 2; i < split.length; i++) {
					Register temp3 = f.allocator.getNext(Register.REFERENCE);
					f.history.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3,
							fileName, line));
					result = temp3;
				}
				
			} else {
				// instance fields of the parameter
				NameNode temp = new NameNode(this.fileName, this.line);
				temp.primaryName = split[0];
				
				temp.compile(s, f);
				Register result = f.allocator.getLast();
				
				for (int i = 1; i < split.length; i++) {
					Register temp3 = f.allocator.getNext(Register.REFERENCE);
					f.history.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3,
							fileName, line));
					
					result = temp3;
				}
			}
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f) throws CompileException {
		
		if (generics != null) {
			throw new CompileException("Generics compiling not supported yet.", fileName, line);
			// TODO
		}
		
		f.history.setName(primaryName);
		// on the left side, get the address
		if (!primaryName.contains(".")) {
			int tableLookup = s.lookup(primaryName);
			if (tableLookup == -1) {
				throw new CompileException("symbol: '" + primaryName + "' not defined.",
						fileName, line);
			}
			String type = s.getType(primaryName);
			 
			if (tableLookup == SymbolTable.local) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetLocalAddressStatement(result, primaryName, fileName, line));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetParamAddressStatement(result, primaryName, fileName, line));
			} else if (tableLookup == SymbolTable.className){
				// load 'this' pointer
				Register thisPointer = f.allocator.getNext(Register.REFERENCE);
				f.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
				
				// load the field from 'this' pointer
				Register result = f.allocator.getNext(type);
				f.statements.add(new GetInstanceFieldAddressStatement(thisPointer, primaryName, result,
						fileName, line));
			}
			// don't do anything
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character

			// construct a primaryExpressionNode
			PrimaryExpressionNode ex = new PrimaryExpressionNode(this.fileName, this.line);

			// set the name - use NoOp since you need something as the prefix.
			f.history.setName(split[0]);
			ex.prefix = new NoOp(this.fileName, this.line);
			ex.suffixes = new ArrayList<>();
			// the rest are consecutive fieldAccesses
			for (int i = 1; i < split.length; i++) {
				FieldExpressionNode field = new FieldExpressionNode(this.fileName, this.line);
				field.identifier = split[i];
				ex.suffixes.add(field);
			}
			
			// compile address the primaryExpressionNode
			ex.compileAddress(s, f);
		}
	}
}
