package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;

/** Represents a CompilationUnit, that is a source file.
*  @author Zach Jones */
public class CompilationUnit implements Node {
    public NameNode packageName;
    public ArrayList<ImportNode> imports = new ArrayList<>();
    public ArrayList<TypeDecNode> types = new ArrayList<>();

    @Override
	public void resolveImports(ClassLookup c) throws IOException {
		// pass down to the types to do
		for (TypeDecNode t : types) {
			t.resolveImports(c);
		}
	}
	
	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// pass down to the types to do
		for (TypeDecNode t : types) {
			t.resolveSymbols(new SymbolTable(null, SymbolTable.className));
		}
		
	}

	/**
	 * Compiles the various types in this CompilationUnit
	 * @return An ArrayList of the types converted to their 
	 * intermediate representation.
	 * @throws CompileException If there is an error compiling this.
	 */
	public ArrayList<InterFile> compile() throws CompileException {
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
