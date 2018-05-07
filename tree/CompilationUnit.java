package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

/** Represents a CompilationUnit, that is a source file.
*  @author Zach Jones */
public class CompilationUnit implements Node {
    public NameNode packageName;
    public ArrayList<ImportNode> imports = new ArrayList<>();
    public ArrayList<TypeDecNode> types = new ArrayList<>();

	public void resolveNames(ClassLookup c) throws IOException {
		// pass down to the types to do
		for (TypeDecNode t : types) {
			t.resolveNames(c);
		}
	}
}
