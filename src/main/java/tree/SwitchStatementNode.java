package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

public class SwitchStatementNode extends NodeImpl implements StatementNode {
    @NotNull private final Expression expression;
	@NotNull private final ArrayList<SwitchStatementPart> parts;
    
    public SwitchStatementNode(@NotNull String fileName, int line,
							   @NotNull Expression expression,
							   @NotNull ArrayList<SwitchStatementPart> parts) {
    	super(fileName, line);
    	this.parts = parts;
    	this.expression = expression;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expression.resolveImports(c);
		for (SwitchStatementPart part : parts) {
			part.resolveImports(c);
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {
		
		throw new CompileException("Switch statement compiling not implemented yet.", getFileName(), getLine());
	}
}
