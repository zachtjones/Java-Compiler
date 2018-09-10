package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;

public class TypeDecNode {
    // only one of these will not be null
    // if they are all null, then the code was just ';'
    public ClassNode c;
    public ClassNode i; // interfaces are treated as abstract classes in this
    public EnumNode e;
    
	public void resolveImports(ClassLookup c1) throws CompileException {
		if (c != null) {
			c.resolveImports(c1);
		} else if (i != null) {
			i.resolveImports(c1);
		} else if (e != null) {
			e.resolveImports(c1);
		}
		
	}

	/**
	 * Passes down the compile job to the class/interface/enum
	 * @param packageName the package's name, or null if default.
	 * @param classLevel The class - level symbols (from imports)
	 * @return The intermediate file node.
	 * @throws CompileException If there is an error compiling 
	 * this type declaration.
	 */
	public InterFile compile(String packageName, SymbolTable classLevel) throws CompileException {
		if (this.c != null) {
			return c.compile(packageName, classLevel);
		} else if (this.i != null) {
			return i.compile(packageName, classLevel);
		} else if (this.e != null){
			return e.compile(packageName, classLevel);
		}
		// empty type declaration (just ; ) , or not implemented yet.
		return null;
	}

	
}
