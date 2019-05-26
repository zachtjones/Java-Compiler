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

/** ++ expr */
public class PreIncrementExpressionNode extends NodeImpl implements StatementExprNode, Expression {
    public Expression expr;
    
    public PreIncrementExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

    	// expr += 1, return the result
		// construct an AssignmentNode:  expr += 1;
		LiteralExpressionNode literal = new LiteralExpressionNode(getFileName(), getLine(), "1");

		AssignmentNode n = new AssignmentNode(getFileName(), getLine(), expr, literal, BinaryOperation.ADD);
		// compile it
		n.compile(s, f);
	}
}
