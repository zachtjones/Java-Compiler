package tree;

/** "super" . fieldName 
 * Note that fields are not inherited, need a way to qualify in IL, 
 * thinking of having a 'super' pointer in the structures. 
 * */
public class SuperFieldExpressionNode implements Expression {
    public String fieldName;
    public String fileName;
    public int line;
    
    public SuperFieldExpressionNode(String fileName, int line) {
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
