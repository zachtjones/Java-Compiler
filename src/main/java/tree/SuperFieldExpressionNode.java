package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** "super" . fieldName 
 * Note that fields are not inherited, need a way to qualify in IL
 */
public class SuperFieldExpressionNode extends NodeImpl implements Expression {
    public String fieldName;

    public SuperFieldExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// nothing needed
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// TODO
		throw new CompileException("super.field not implemented yet", getFileName(), getLine());
	}
}
