package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterStatement;

public class ClassBodyNode implements Node {
    // one of these will be not null
    public BlockNode staticInit;
    public ConstructorNode constructor;
    public MethodNode method;
    public FieldDeclarationNode field;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
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

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// pass down
		if (staticInit != null) {
			staticInit.resolveSymbols(s);
		} else if (constructor != null) {
			constructor.resolveSymbols(s);
		} else if (method != null) {
			method.resolveSymbols(s);
		} else if (field != null) {
			field.resolveSymbols(s);
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
			ArrayList<InterStatement> statements = staticInit.compile();
			for (InterStatement i : statements) {
				f.addStaticInit(i);
			}
		} else if (constructor != null) {
			constructor.compile(f);
		} else if (method != null) {
			method.compile(f, syms);
		} else if (field != null) {
			field.compile(f, syms);
		}
	}

}
