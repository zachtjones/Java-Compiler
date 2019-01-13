package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;

public class ClassBodyNode {
    // one of these will be not null
    public BlockNode staticInit;
    public ConstructorNode constructor;
    public MethodNode method;
    public FieldDeclarationNode field;

	public void resolveImports(ClassLookup c) throws CompileException {
		// pass down
		if (staticInit != null) {
			staticInit.resolveImports(c);
		} else if (constructor != null) {
			constructor.resolveImports(c);
		} else if (method != null) {
			method.resolveImports(c);
		} else if (field != null) {
			field.resolveImports(c);
		}
	}

	/**
	 * Compiles this class body node into the intermediate file.
	 * @param f The intermediate file to compile into
	 * @param syms The symbol table - place entries into it.
	 * @throws CompileException If there is an error during the compiling phase.
	 */
	public void compile(InterFile f, SymbolTable syms) throws CompileException {
		// pass down
		if (staticInit != null) {
			InterFunction func = new InterFunction();
			staticInit.compile(syms, func);
			f.addFunction(func);
		} else if (constructor != null) {
			constructor.compile(f, syms);
		} else if (method != null) {
			method.compile(f, syms);
		} else if (field != null) {
			field.compile(f, syms);
		}
	}

}
