package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** condition ? truePart : falsePart */
public class ConditionalExpressionNode extends NodeImpl implements Expression {
    public Expression condition;
    public Expression truePart;
    public Expression falsePart;

    public ConditionalExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		condition.resolveImports(c);
		truePart.resolveImports(c);
		falsePart.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {
		
		// TODO
		throw new CompileException("conditional ternary not implemented yet", getFileName(), getLine());
	}
	
}
