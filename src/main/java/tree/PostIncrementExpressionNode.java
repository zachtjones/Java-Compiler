package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;

/** expr ++ */
public class PostIncrementExpressionNode implements StatementExprNode, Expression {
    public Expression expr;
    public String fileName;
    public int line;
    
    public PostIncrementExpressionNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		expr.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// construct an AssignmentNode:  expr += 1;
		AssignmentNode n = new AssignmentNode(fileName, line);
		n.left = expr;
		n.type = AssignmentNode.PLUSASSIGN;
		LiteralExpressionNode literal = new LiteralExpressionNode(fileName, line);
		literal.value = "1";
		n.right = literal;
		
		// compile it
		n.compile(s, f);
	}

}
