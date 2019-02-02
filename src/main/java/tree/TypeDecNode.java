package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypeDecNode {

	/** Resolves the imports, making all the names/types fully qualified.
	 * @param c The ClassLookup instance for transforming symbol names */
	void resolveImports(@NotNull ClassLookup c) throws CompileException;

	/** Compiles this type declaration into an intermediate language file 
	 * @throws CompileException If there is an error compiling */
	InterFile compile(@Nullable String packageName, @NotNull SymbolTable classLevel) throws CompileException;

}
