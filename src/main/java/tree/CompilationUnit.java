package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Represents a CompilationUnit, that is a source file.
*  @author Zach Jones */
public class CompilationUnit {
    @Nullable public final NameNode packageName; // TODO make this private, create methods here for other needs
    @NotNull private final ArrayList<ImportNode> imports;
    @NotNull private final ArrayList<TypeDecNode> types;
    @NotNull private final String fileName;
    private final int line;
    
    public CompilationUnit(@NotNull String fileName, int line, @Nullable NameNode packageName,
						   @NotNull ArrayList<ImportNode> imports, @NotNull ArrayList<TypeDecNode> types) {
    	this.fileName = fileName;
    	this.line = line;
		this.packageName = packageName;
		this.imports = imports;
		this.types = types;
	}

	/**
	 * Compiles the various types in this CompilationUnit
	 * @param newFileName The filename that represents the file's name crated in on list
	 * @return An ArrayList of the types converted to their
	 * intermediate representation.
	 * @throws CompileException If there is an error compiling this.
	 */
	public ArrayList<InterFile> compile(String newFileName) throws CompileException {

		// update the class lookup tables (short names to full names)
		SymbolTable classLevel = new SymbolTable(null, SymbolTable.className);
		ClassLookup lookup = new ClassLookup(newFileName, packageName, imports, classLevel);

		// resolve the imports -- placing resolved names into the global symbol table

		// pass down to the types to do
		for (TypeDecNode t : types) {
			t.resolveImports(lookup);
		}


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
