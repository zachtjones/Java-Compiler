package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import intermediate.PutLocalStatement;
import intermediate.StartScopeStatement;
import org.jetbrains.annotations.NotNull;

public class LocalVariableDecNode extends NodeImpl implements StatementNode {
    @NotNull private Types type;
    @NotNull private final ArrayList<VariableDecNode> declarations;
    
    public LocalVariableDecNode(String fileName, int line, @NotNull Types type,
								@NotNull ArrayList<VariableDecNode> declarations) {
    	super(fileName, line);
    	this.type = type;
    	this.declarations = declarations;
    }
    
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		type = type.resolveImports(c, getFileName(), getLine());
		for (VariableDecNode d : declarations) {
			if (d.init != null) {
				d.init.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// place in symbol table and add intermediate code
		for (VariableDecNode d : declarations) {
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
