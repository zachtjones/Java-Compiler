package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
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

	public TypeNode type;
	public ArrayList<VariableDecNode> variables = new ArrayList<>();

    public FieldDeclarationNode(String fileName, int line) {
    	super(fileName, line);
    }

	public void resolveImports(ClassLookup c) throws CompileException {
		type.resolveImports(c);
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
		String typeRep = type.interRep();
		for (VariableDecNode d : variables) {
			// fix: String[] id[] -> String[][] id;
			StringBuilder temp = new StringBuilder(typeRep);
			for (int i = 0; i < d.id.numDimensions; i++) {
				temp.append("[]");
			}
			f.addField(temp.toString(), d.id.name, isStatic);
			syms.putEntry(d.id.name, typeRep, getFileName(), getLine());

			// add the initial values if any
			if (d.init != null && d.init.e != null) {
				// construct the assignment to the expression
				NameNode n = new NameNode(getFileName(), getLine());
				n.primaryName = d.id.name;

				// the type is null here
				AssignmentNode a = new AssignmentNode(getFileName(), getLine(), n, d.init.e, null);

				// compile the created expression.
				InterFunction func = new InterFunction();
				// add instance initializers
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
	void putSymbols(SymbolTable classLevel) throws CompileException {
		String type = isStatic ? "staticField" : "instanceField";
		for (VariableDecNode variable : variables) {
			classLevel.putEntry(variable.id.name, type, getFileName(), getLine());
		}
	}
}
