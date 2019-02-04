package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** condition ? truePart : falsePart */
public class ConditionalExpressionNode extends NodeImpl implements Expression {
    @NotNull private final Expression condition, truePart, falsePart;

    public ConditionalExpressionNode(String fileName, int line, @NotNull Expression condition,
									 @NotNull Expression truePart, @NotNull Expression falsePart) {
    	super(fileName, line);
		this.condition = condition;
		this.truePart = truePart;
		this.falsePart = falsePart;
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
