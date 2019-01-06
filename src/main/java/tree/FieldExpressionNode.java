package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.GetInstanceFieldAddressStatement;
import intermediate.GetInstanceFieldStatement;
import intermediate.GetParamStatement;
import intermediate.GetStaticFieldAddressStatement;
import intermediate.GetStaticFieldStatement;
import intermediate.InterFunction;
import intermediate.Register;

/** Represents accessing a field of an object. */
public class FieldExpressionNode implements Expression, LValue {
    public String identifier;
    private final String fileName;
    private final int line;
    
    public FieldExpressionNode(String fileName, int line) {
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
		// identifier is a variable name, don't need to resolve imports
	}

	@Override
	public void compile(SymbolTable s, InterFunction f)
			throws CompileException {
		
		if (f.history.wasThisLast()) {
			// check symbol table -- declared in the same compilation unit
			// TODO check inheritance
			int tableLookup = s.lookup(identifier);
			if (tableLookup == -1) {
				throw new CompileException("symbol: " + identifier + " is not defined before use.",
						fileName, line);
			}
			String type = s.getType(identifier);
			
			// load 'this' pointer
			Register thisPointer = f.allocator.getNext(Register.REFERENCE);
			f.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
			
			// load the field from 'this' pointer
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetInstanceFieldStatement(thisPointer, identifier, result,
					fileName, line));
			
		} else if (f.history.getName() != null) {
			int tableLookup = s.lookup(f.history.getName());
			if (tableLookup == SymbolTable.className) {
				// static lookup
				f.statements.add(
						new GetStaticFieldStatement(f.history.getName(), identifier, f.allocator.getNext("unknown"),
								fileName, line));
			} else {
				// instance field of symbol name
				// get the address of the object
				NameNode n = new NameNode(fileName, line);
				n.primaryName = f.history.getName();
				n.compile(s, f);
				Register name = f.allocator.getLast();
				f.statements.add(new GetInstanceFieldStatement(name, identifier, f.allocator.getNext("unknown"),
						fileName, line));
			}
			f.history.setName(identifier);
		} else {
			throw new CompileException("Expression compilation of object.FIELD not done yet.",
					fileName, line);
		}
	}

	@Override
	public void compileAddress(SymbolTable s, InterFunction f)
			throws CompileException {
		if (f.history.wasThisLast()) {
			// check symbol table -- declared in the same compilation unit
			// TODO check inheritance
			int tableLookup = s.lookup(identifier);
			if (tableLookup == -1) {
				throw new CompileException("symbol: " + identifier + " is not defined before use.",
						fileName, line);
			}
			String type = s.getType(identifier);
			
			// load 'this' pointer
			Register thisPointer = f.allocator.getNext(Register.REFERENCE);
			f.statements.add(new GetParamStatement(thisPointer, "this", fileName, line));
			
			// load the field from 'this' pointer
			Register result = f.allocator.getNext(type);
			f.statements.add(new GetInstanceFieldAddressStatement(thisPointer, identifier, result,
					fileName, line));
			
		} else if (f.history.getName() != null) {
			int tableLookup = s.lookup(f.history.getName());
			if (tableLookup == SymbolTable.className) {
				// static lookup
				f.statements.add(
					new GetStaticFieldAddressStatement(f.history.getName(), identifier, f.allocator.getNext("unknown"),
							fileName, line));
			} else {
				// instance field of symbol name
				// get the address of the object
				NameNode n = new NameNode(fileName, line);
				n.primaryName = f.history.getName();
				n.compileAddress(s, f);
				Register name = f.allocator.getLast();
				f.statements.add(
					new GetInstanceFieldAddressStatement(name, identifier, f.allocator.getNext("unknown"),
							fileName, line));
			}
			f.history.setName(identifier);
		} else {
			throw new CompileException("Expression compilation of object.FIELD not done yet.",
					fileName, line);
		}
	}
}
