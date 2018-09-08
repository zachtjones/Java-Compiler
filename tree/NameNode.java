package tree;

import java.io.IOException;
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
import intermediate.RegisterAllocator;

public class NameNode implements Node, Expression, LValue {
	public String primaryName;
	public NameNode extendsNode; // used in class / interface declarations
	public ArrayList<NameNode> generics;
	public NameNode secondaryName;
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

	/** Gets the simple name (like java.lang) for this name node,
	 * using the secondary name if needed.
	 * Generics are not filled in */
	public String getSimpleName() {
		StringBuilder result = new StringBuilder(primaryName);
		if (secondaryName != null) {
			result.append('.');
			result.append(secondaryName.getSimpleName());
		}
		return result.toString();
	}

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// only the first part - the primaryName could be not fully qualified
		//   in the example: System.out.println
		//System.out.print("Replace: " + this.getSimpleName());
		String firstFull = null;
		try {
			firstFull = c.getFullName(primaryName);
		} catch (CompileException e) { /* will never happen */ }
		
		String total = firstFull;
		if (secondaryName != null) {
			String rest = secondaryName.getSimpleName();
			total += '.' + rest;
		}
		// have resolved it all
		this.primaryName = total;
		this.secondaryName = null;
		//System.out.println(" -> " + total);
		// TODO handle generics and the ? extends / super thing
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		if (!primaryName.contains(".")) {
			// on the right side, get the result
			int tableLookup = s.lookup(primaryName);
			String type;
			if (tableLookup == -1) {
				type = "unknown";
			} else {
				type = s.getType(primaryName);
			}
			if (tableLookup == SymbolTable.local) {
				Register result = r.getNext(type);
				f.statements.add(new GetLocalStatement(result, primaryName));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = r.getNext(type);
				f.statements.add(new GetParamStatement(result, primaryName));
			} else { // assume static/instance field / method
				// load 'this' pointer
				Register thisPointer = r.getNext(Register.REFERENCE);
				f.statements.add(new GetParamStatement(thisPointer, "this"));
				
				// load the field from 'this' pointer
				Register result = r.getNext(type);
				f.statements.add(new GetInstanceFieldStatement(thisPointer, primaryName, result));
				c.setName(primaryName);
			}
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character
			int tableLookup = s.lookup(split[0]);
			if (tableLookup == SymbolTable.className) {
				// get the static field, then do the chain of instance fields
				Register result = r.getNext(Register.REFERENCE);
				f.statements.add(new GetStaticFieldStatement(split[0], split[1], result));
				c.setName(split[1]);
				for (int i = 2; i < split.length; i++) {
					Register temp3 = r.getNext(Register.REFERENCE);
					c.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3));
					result = temp3;
				}
				
			} else {
				// instance fields of the parameter
				NameNode temp = new NameNode(this.fileName, this.line);
				temp.primaryName = split[0];
				
				temp.compile(s, f, r, c);
				Register result = r.getLast();
				
				for (int i = 1; i < split.length; i++) {
					Register temp3 = r.getNext(Register.REFERENCE);
					c.setName(split[i]);
					f.statements.add(new GetInstanceFieldStatement(result, split[i], temp3));
					result = temp3;
				}
			}
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		c.setName(primaryName);
		// on the left side, get the address
		if (!primaryName.contains(".")) {
			int tableLookup = s.lookup(primaryName);
			String type;
			if (tableLookup == -1) {
				type = "unknown";
			} else {
				type = s.getType(primaryName);
			} 
			if (tableLookup == SymbolTable.local) {
				Register result = r.getNext(type);
				f.statements.add(new GetLocalAddressStatement(result, primaryName));
			} else if (tableLookup == SymbolTable.parameter) {
				Register result = r.getNext(type);
				f.statements.add(new GetParamAddressStatement(result, primaryName));
			} else if (tableLookup == SymbolTable.className){
				// load 'this' pointer
				Register thisPointer = r.getNext(Register.REFERENCE);
				f.statements.add(new GetParamStatement(thisPointer, "this"));
				
				// load the field from 'this' pointer
				Register result = r.getNext(type);
				f.statements.add(new GetInstanceFieldAddressStatement(thisPointer, primaryName, result));
			}
			// don't do anything
		} else {
			// split it by the .
			String[] split = primaryName.split("\\."); // split by the . character

			// construct a primaryExpressionNode
			PrimaryExpressionNode ex = new PrimaryExpressionNode(this.fileName, this.line);

			// set the name - use NoOp since you need something as the prefix.
			c.setName(split[0]);
			ex.prefix = new NoOp(this.fileName, this.line);
			ex.suffixes = new ArrayList<Expression>();
			// the rest are consecutive fieldAccesses
			for (int i = 1; i < split.length; i++) {
				FieldExpressionNode field = new FieldExpressionNode(this.fileName, this.line);
				field.identifier = split[i];
				ex.suffixes.add(field);
			}
			
			// compile address the primaryExpressionNode
			ex.compileAddress(s, f, r, c);
		}
	}
}
