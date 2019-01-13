package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** expr -- */
public class PostDecrementExpressionNode extends NodeImpl implements StatementExprNode, Expression {
    public Expression expr;
    
    public PostDecrementExpressionNode(String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// construct an AssignmentNode:  expr += 1;
		AssignmentNode n = new AssignmentNode(getFileName(), getLine());
		n.left = expr;
		n.type = AssignmentNode.MINUSASSIGN;
		LiteralExpressionNode literal = new LiteralExpressionNode(getFileName(), getLine());
		literal.value = "1";
		n.right = literal;
		
		// compile it
		n.compile(s, f);
	}
}
