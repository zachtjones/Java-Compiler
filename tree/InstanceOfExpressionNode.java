package tree;

/* left instanceof right */
public class InstanceOfExpressionNode implements Expression {
    public Expression left;
    public TypeNode right;
    public String fileName;
    public int line;
    
    public InstanceOfExpressionNode(String fileName, int line) {
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
