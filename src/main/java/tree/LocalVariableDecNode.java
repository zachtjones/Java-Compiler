package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import intermediate.PutLocalStatement;
import intermediate.StartScopeStatement;

public class LocalVariableDecNode extends NodeImpl {
    public Types type;
    public ArrayList<VariableDecNode> declarators;
    
    public LocalVariableDecNode(String fileName, int line) {
    	super(fileName, line);
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		type.resolveImports(c);
		for (VariableDecNode d : declarators) {
			if (d.init != null) {
				d.init.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// place in symbol table and add intermediate code
		for (VariableDecNode d : declarators) {
			s.putEntry(d.id.name, type, getFileName(), getLine());
			f.statements.add(new StartScopeStatement(d.id.name, type));
			if (d.init != null) {
				// put in the code to compute initial value.
				d.init.compile(s, f);
				// store the initial value into the local variable.
				f.statements.add(new PutLocalStatement(f.allocator.getLast(), d.id.name, getFileName(), getLine()));
			}
		}
	}
}
