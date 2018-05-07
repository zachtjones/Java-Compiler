package tree;

import java.io.IOException;

import helper.ClassLookup;

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

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// don't do anything
	}
}
