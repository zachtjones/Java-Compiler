package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import intermediate.InterFunction;

public class FieldDeclarationNode extends NodeImpl {
	public boolean isPublic;
	public boolean isProtected;
	public boolean isPrivate;
	public boolean isStatic;
	public boolean isFinal;
	public boolean isTransient;
	public boolean isVolatile;

	public Types type;
	public ArrayList<VariableDecNode> variables = new ArrayList<>();

    public FieldDeclarationNode(String fileName, int line) {
    	super(fileName, line);
    }

	public void resolveImports(ClassLookup c) throws CompileException {
		type = type.resolveImports(c, getFileName(), getLine());
	}

	/**
	 * Compiles this into the Intermediate file
	 * @param f the intermediate file to compile into.
	 * @param syms The symbol table entry to put the field declaration into.
	 * @throws CompileException if there is a compiling exception thrown.
	 */
	public void compile(InterFile f, SymbolTable syms) throws CompileException {
		// add the type declarations to the instance structure, and the
		//  initial values to the instance init
		for (VariableDecNode d : variables) {
			// fix: String[] id[] -> String[][] id;
			Types temp = type;
			for (int i = 0; i < d.id.numDimensions; i++) {
				temp = Types.arrayOf(temp);
			}
			f.addField(temp, d.id.name, isStatic);

			// add the initial values if any
			if (d.init != null && d.init.e != null) {
				// construct the assignment to the expression
				NameNode n = new NameNode(getFileName(), getLine());
				n.primaryName = d.id.name;

				// the type is null here
				AssignmentNode a = new AssignmentNode(getFileName(), getLine(), n, d.init.e, null);

				// add instance initializer
				final String name = isStatic ? "<clinit>" : "<init>"; // following java .class standard here
				// compile the created expression.
				InterFunction func = new InterFunction(f.getName(), name, Types.VOID);
				func.isInit = true;
				func.isInstance = !isStatic;
				a.compile(syms, func);
				f.addFunction(func);
			}
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		/* Nothing to do here*/
	}

	/** places the symbols declared by this field declaration */
	void putSymbols(SymbolTable staticFields, SymbolTable instanceFields) throws CompileException {
		// choose which one to place them in
		SymbolTable choice = isStatic ? staticFields : instanceFields;
		for (VariableDecNode variable : variables) {
			choice.putEntry(variable.id.name, type, getFileName(), getLine());
		}
	}
}
