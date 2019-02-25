package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** (type[]...)expr */
public class CastExpressionNode extends NodeImpl implements Expression {
    @NotNull private final Expression expr;
    @NotNull private Types type;

    public CastExpressionNode(@NotNull String fileName, int line, @NotNull Types type, @NotNull Expression expr) {
    	super(fileName, line);
    	this.type = type;
    	this.expr = expr;
    }
    
    @Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);
		type = type.resolveImports(c, getFileName(), getLine());
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// TODO
		throw new CompileException("Casting not implemented yet.", getFileName(), getLine());
	}
}
