package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class MethodNode {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;
    public boolean isStatic;
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isNative;
    public boolean isSynchronized;
    public ResultTypeNode resultType;

    public MethodDeclaratorNode dec;
    public ArrayList<NameNode> throwsList;
    public BlockNode code;
    
	
	public void resolveImports(ClassLookup c) throws IOException {
		resultType.resolveImports(c);
		dec.resolveImports(c);
		if (throwsList != null) {
			for (NameNode n : throwsList) {
				n.resolveImports(c);
			}
		}
		code.resolveImports(c);
	}

	/**
	 * Compile this method into the intermediate file.
	 * @param f The IL file.
	 * @param syms The symbol table entry to place the method declaration into.
	 * @throws CompileException If there is a compilation error.
	 */
	public void compile(InterFile f, SymbolTable syms) throws CompileException {
		InterFunction func = new InterFunction();
		if (isNative) {
			throw new CompileException("native methods not implemented yet.");
		}
		
		// TODO - final and synchronized, ... modifiers
		
		RegisterAllocator r = new RegisterAllocator();
		
		// create new scope, use the declaratorNode to add to the new scope
		SymbolTable paramTable = new SymbolTable(syms, SymbolTable.parameter);
		dec.compile(paramTable, func, r);
		
		// create new scope under the parameters for the code
		SymbolTable codeTable = new SymbolTable(paramTable, SymbolTable.local);
		code.compile(codeTable, func, r);
		f.addFunction(func);
	}


}
