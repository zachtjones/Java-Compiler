package tree;

/** (type[]...)expr */
public class PrimitiveCastExpressionNode implements Expression {
    public Expression expr;
    public PrimitiveTypeNode type;
    public int arrayDims = 0;
    public String fileName;
    public int line;
    
    public PrimitiveCastExpressionNode(String fileName, int line) {
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
