package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import intermediate.InterFile;

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

	/**
	 * Compiles the various types in this CompilationUnit
	 * @return An ArrayList of the types converted to their 
	 * intermediate representation.
	 */
	public ArrayList<InterFile> compile() {
		ArrayList<InterFile> a = new ArrayList<InterFile>();
		// names are already resolved, so there should be no need
		//   to pass down the imports -- everything can be figured out
		//   even with expressions like a.b.c, can still find out the type
		//   of b
		
		for (TypeDecNode t : types) {
			if (packageName != null) {
				a.add(t.compile(packageName.getSimpleName()));
			} else {
				a.add(t.compile(null));
			}
		}
		
		return a;
	}
}
