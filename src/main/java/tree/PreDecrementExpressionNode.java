package tree;

import helper.BinaryOperation;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.LoadLiteralStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import static helper.BinaryOperation.SUBTRACT;

/** -- expr */
public class PreDecrementExpressionNode extends NodeImpl implements StatementExprNode, Expression {
    public Expression expr;

    public PreDecrementExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		expr.resolveImports(c);		
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// construct an AssignmentNode:  expr -= 1;
		LiteralExpressionNode literal = new LiteralExpressionNode(getFileName(), getLine(), "1");

		AssignmentNode n = new AssignmentNode(getFileName(), getLine(), expr, literal, BinaryOperation.SUBTRACT);
		// compile it
		n.compile(s, f);
	}
}
