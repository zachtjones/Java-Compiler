package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** Represents a CompilationUnit, that is a source file.
*  @author Zach Jones */
public class CompilationUnit extends NodeImpl implements Node {
    public NameNode packageName;
    public ArrayList<ImportNode> imports = new ArrayList<>();
    public ArrayList<TypeDecNode> types = new ArrayList<>();
    public String fileName;
    public int line;
    
    public CompilationUnit(String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// pass down to the types to do
		for (TypeDecNode t : types) {
			t.resolveImports(c);
		}
	}
	
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// don't call this method.
	}

	/**
	 * Compiles the various types in this CompilationUnit
	 * @param classLevel The class level symbol table (imported)
	 * @return An ArrayList of the types converted to their 
	 * intermediate representation.
	 * @throws CompileException If there is an error compiling this.
	 */
	public ArrayList<InterFile> compile(SymbolTable classLevel) throws CompileException {
		ArrayList<InterFile> a = new ArrayList<>();
		// names are already resolved, so there should be no need
		//   to pass down the imports -- everything can be figured out
		//   even with expressions like a.b.c, can still find out the type
		//   of b
		
		for (TypeDecNode t : types) {
			InterFile i;
			if (packageName != null) {
				i = t.compile(packageName.primaryName.replace('.', '/'), classLevel);
			} else {
				i = t.compile(null, classLevel);
			}
			a.add(i);
		}
		
		return a;
	}

	
}
