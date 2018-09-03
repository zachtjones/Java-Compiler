package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** Represents a primitive type */
public class PrimitiveTypeNode implements Node {
    public static final int BOOLEAN = 0;
    public static final int CHAR = 1;
    public static final int BYTE = 2;
    public static final int SHORT = 3;
    public static final int INT = 4;
    public static final int LONG = 5;
    public static final int FLOAT = 6;
    public static final int DOUBLE = 7;

    public int type; // holds value 0-7 inclusive
    public String fileName;
    public int line;
    
    public PrimitiveTypeNode(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) throws IOException {
		// don't do anything
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// nothing to do here.
	}
	
	/**
	 * Returns the intermediate file's representation for this type.
	 */
	public String interRep() {
		switch(type) {
		case BOOLEAN: return "bool";
		case CHAR: return "char";
		case BYTE: return "byte";
		case SHORT: return "short";
		case INT: return "int";
		case LONG: return "long";
		case FLOAT: return "float";
		case DOUBLE: return "double";
		}
		return "Error: primitive node type not recognized";
	}

	
}
