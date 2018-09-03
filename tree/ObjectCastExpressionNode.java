package tree;

/** (type[]...)expr */
public class ObjectCastExpressionNode implements Expression {
    public Expression expr;
    public NameNode type;
    public int arrayDims = 0;
    public String fileName;
    public int line;
    
    public ObjectCastExpressionNode(String fileName, int line) {
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
