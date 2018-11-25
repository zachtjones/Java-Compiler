package tree;

import helper.CompileException;
import intermediate.InterFile;

public interface TypeDecNode extends Node {

	/** Compiles this type declaration into an intermediate language file 
	 * @throws CompileException If there is an error compiling */
	InterFile compile(String packageName, SymbolTable classLevel) throws CompileException;

}
