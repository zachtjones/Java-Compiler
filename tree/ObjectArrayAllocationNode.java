package tree;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class ObjectArrayAllocationNode implements Expression {
    public NameNode type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
    public String fileName;
    public int line;
    
    public ObjectArrayAllocationNode(String fileName, int line) {
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
