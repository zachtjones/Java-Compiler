package tree;

import helper.ArrayDimensions;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class TypeNode implements Node {
    // one of these two will not be null
    public PrimitiveTypeNode primitive;
    public NameNode name;

    public int arrayDims; // 0 for not array;
    public String fileName;
    public int line;
    
    public TypeNode(String fileName, int line) {
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
		if (name != null) {
			name.resolveImports(c);
		}
	}
	
	/** Returns the intermediate language representation of this */
	public String getILRep() {
		if (name != null) {
			return name.primaryName;
		} else {
			return primitive.interRep();
		}
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// this is already done in higher nodes.
	}
	
	/**
	 * Gets the IL representation of this type.
	 */
	public String interRep() {
		if (primitive != null) {
			return primitive.interRep() + ArrayDimensions.get(arrayDims);
		}
		return name.primaryName + ArrayDimensions.get(arrayDims);
	}

	
}
