package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetInstanceFieldAddressStatement;
import intermediate.GetInstanceFieldStatement;
import intermediate.GetStaticFieldAddressStatement;
import intermediate.GetStaticFieldStatement;
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
		} else if (c.getName() != null) {
			int tableLookup = s.lookup(c.getName());
			if (tableLookup == SymbolTable.className) {
				// static lookup
				f.statements.add(new GetStaticFieldStatement(c.getName(), identifier, r.getNext("unknown")));
			} else {
				// instance field of symbol name
				// get the address of the object
				NameNode n = new NameNode();
				n.primaryName = c.getName();
				n.compileAddress(s, f, r, c);
				Register name = r.getLast();
				f.statements.add(new GetInstanceFieldStatement(name, identifier, r.getNext("unknown")));
			}
			c.setName(identifier);
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
		} else if (c.getName() != null) {
			int tableLookup = s.lookup(c.getName());
			if (tableLookup == SymbolTable.className) {
				// static lookup
				f.statements.add(new GetStaticFieldAddressStatement(c.getName(), identifier, r.getNext("unknown")));
			} else {
				// instance field of symbol name
				// get the address of the object
				NameNode n = new NameNode();
				n.primaryName = c.getName();
				n.compileAddress(s, f, r, c);
				Register name = r.getLast();
				f.statements.add(new GetInstanceFieldAddressStatement(name, identifier, r.getNext("unknown")));
			}
			c.setName(identifier);
		} else {
			throw new CompileException("Expression compilation of object.FIELD not done yet.");
		}
	}
}
