package tree;

import java.io.IOException;

import helper.ClassLookup;

public interface Node {
	/** Writes all names as a string with the fully qualified name */
    public void resolveNames(ClassLookup c) throws IOException;
}
