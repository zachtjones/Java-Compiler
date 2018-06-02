package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetThisFieldAddressStatement;
import intermediate.GetThisFieldStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** Represents accessing a field of an object. */
public class FieldExpressionNode implements Expression, LValue {
    public String identifier;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// identifier is a variable name, don't need to resolve imports
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		if (c.wasThisLast()) {
			// check symbol table -- declared in the same compilation unit
			// TODO check inheritance
			int tableLookup = s.lookup(identifier);
			if (tableLookup == -1) {
				throw new CompileException("symbol: " + identifier + " is not defined before use.");
			}
			String type = s.getType(identifier);
			Register result = r.getNext(type);
			f.statements.add(new GetThisFieldStatement(result, identifier));
		} else {
			throw new CompileException("Expression compilation of object.FIELD not done yet.");
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c)
			throws CompileException {
		if (c.wasThisLast()) {
			// check symbol table -- declared in the same compilation unit
			// TODO check inheritance
			int tableLookup = s.lookup(identifier);
			if (tableLookup == -1) {
				throw new CompileException("symbol: " + identifier + " is not defined before use.");
			}
			String type = s.getType(identifier);
			Register result = r.getNext(type);
			f.statements.add(new GetThisFieldAddressStatement(result, identifier));
		} else {
			throw new CompileException("Expression compilation of object.FIELD not done yet.");
		}
	}
}
