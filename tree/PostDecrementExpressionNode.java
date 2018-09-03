package tree;

/** expr -- */
public class PostDecrementExpressionNode implements Expression {
    public Expression expr;
    public String fileName;
    public int line;
    
    public PostDecrementExpressionNode(String fileName, int line) {
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
}
