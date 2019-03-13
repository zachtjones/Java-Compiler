package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public class MethodNode {
	@NotNull private final String fileName;
	private final int line;

	public boolean isPublic;
    private final boolean isProtected;
    private final boolean isPrivate;
    private final boolean isStatic;
    private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean isNative;
    private final boolean isSynchronized;
	private Types returnType;
	@NotNull private final String name;
	@NotNull private final ArrayList<ParamNode> params;

    @NotNull
	private ArrayList<NameNode> throwsList; // null if no list
	@Nullable private final BlockNode content; // null if body is ';'

    public MethodNode(@NotNull String fileName, int line,
					  boolean isPublic, boolean isProtected, boolean isPrivate, boolean isStatic, boolean isAbstract,
					  boolean isFinal, boolean isNative, boolean isSynchronized,
					  @NotNull Types returnType, @NotNull String name, @NotNull ArrayList<ParamNode> params,
					  @Nullable ArrayList<NameNode> throwsList, @Nullable BlockNode content) {
		this.fileName = fileName;
		this.line = line;


		this.isPublic = isPublic;
		this.isProtected = isProtected;
		this.isPrivate = isPrivate;
		this.isStatic = isStatic;
		this.isAbstract = isAbstract;
		this.isFinal = isFinal;
		this.isNative = isNative;
		this.isSynchronized = isSynchronized;
		this.returnType = returnType;
		this.name = name;
		this.params = params;
		this.throwsList = throwsList == null ? new ArrayList<>() : throwsList;
		this.content = content;
	}
    
	
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		returnType = returnType.resolveImports(c, fileName, line);
		for (NameNode n : throwsList) {
			n.resolveImports(c);
		}
		for (ParamNode p : params) {
			p.resolveImports(c);
		}
		if (content != null) {
			content.resolveImports(c);
		}
	}

	/**
	 * Compile this method into the intermediate file.
	 * @param f The IL file.
	 * @param syms The symbol table entry to place the method declaration into.
	 * @throws CompileException If there is a compilation error.
	 */
	public void compile(@NotNull InterFile f, @NotNull SymbolTable syms) throws CompileException {
		InterFunction func = new InterFunction(f.getName(), name, returnType);
		if (isNative) {
			throw new CompileException("native methods not implemented yet.", "", -1);
		}
		
		func.isInstance = !isStatic;

		// TODO - final and synchronized, ... modifiers
		
		// create new scope, use the declaratorNode to add to the new scope
		SymbolTable paramTable = new SymbolTable(syms, SymbolTable.parameter);
		for (ParamNode p : params) {
			paramTable.putEntry(p.id.name, p.type, fileName, line);
			func.paramTypes.add(p.type);
			func.paramNames.add(p.id.name);
			if (p.isVarargs) {
				if (func.lastArgVarargs) { // can only have one argument varargs, and has to be last
					throw new CompileException(
						"the variable arguments can only be used on the last parameter.",
						fileName, line);
				}
				func.lastArgVarargs = true;
			} else {
				if (func.lastArgVarargs)
					// make sure that a previous argument was not ...
					throw new CompileException("the variable arguments can onlybe on the last paramter.",
						fileName, line);
			}
		}

		// create new scope under the parameters for the code
		if (content != null) {
			SymbolTable codeTable = new SymbolTable(paramTable, SymbolTable.local);
			content.compile(codeTable, func);

			// done with codeTable
			codeTable.endScope(func);
		}

		// done with param table
		paramTable.endScope(func);

		// done with the method
		f.addFunction(func);
	}


	void putSymbols(@NotNull SymbolTable classLevel) throws CompileException {
		classLevel.putEntry(name, isStatic ? Types.STATIC_FUNCTION : Types.INSTANCE_FUNCTION, fileName, line);
	}
}
