package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetLocalAddressStatement;
import intermediate.GetLocalStatement;
import intermediate.GetParamAddressStatement;
import intermediate.GetParamStatement;
import intermediate.GetThisFieldAddressStatement;
import intermediate.GetThisFieldStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

public class NameNode implements Node, Expression, LValue {
	public String primaryName;
	public NameNode extendsNode; // used in class / interface declarations
	public ArrayList<NameNode> generics;
	public NameNode secondaryName;

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
		String firstFull = c.getFullName(primaryName);
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
		// on the right side, get the result
		int tableLookup = s.lookup(primaryName);
		if (tableLookup == -1) {
			throw new CompileException("symbol: " + primaryName + " is not defined before use.");
		}
		String type = s.getType(primaryName);
		Register result = r.getNext(type);
		if (tableLookup == SymbolTable.local) {
			f.statements.add(new GetLocalStatement(result, primaryName));
		} else if (tableLookup == SymbolTable.parameter) {
			f.statements.add(new GetParamStatement(result, primaryName));
		} else if (tableLookup == SymbolTable.className){
			f.statements.add(new GetThisFieldStatement(result, primaryName));
		} else {
			throw new CompileException("don't know what to do for symbol: " + primaryName + " in NameNode.java");
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// on the right side, get the result
		int tableLookup = s.lookup(primaryName);
		if (tableLookup == -1) {
			throw new CompileException("symbol: " + primaryName + " is not defined before use.");
		}
		String type = s.getType(primaryName);
		Register result = r.getNext(type);
		if (tableLookup == SymbolTable.local) {
			f.statements.add(new GetLocalAddressStatement(result, primaryName));
		} else if (tableLookup == SymbolTable.parameter) {
			f.statements.add(new GetParamAddressStatement(result, primaryName));
		} else if (tableLookup == SymbolTable.className){
			f.statements.add(new GetThisFieldAddressStatement(result, primaryName));
		} else {
			throw new CompileException("don't know what to do for symbol: " + primaryName + " in NameNode.java");
		}
	}
}
