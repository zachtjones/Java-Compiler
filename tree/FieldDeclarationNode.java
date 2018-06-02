package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class FieldDeclarationNode {
	public boolean isPublic;
	public boolean isProtected;
	public boolean isPrivate;
	public boolean isStatic;
	public boolean isFinal;
	public boolean isTransient;
	public boolean isVolatile;

	public TypeNode type;
	public ArrayList<VariableDecNode> variables = new ArrayList<>();

	public void resolveImports(ClassLookup c) throws IOException {
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
			String temp = typeRep;
			for (int i = 0; i < d.id.numDimensions; i++) {
				temp += "[]";
			}
			f.addField(temp, d.id.name, isStatic);
			syms.putEntry(d.id.name, typeRep);

			// add the initial values if any
			if (d.init != null && d.init.e != null) {
				// construct the assignment to the expression
				AssignmentNode a = new AssignmentNode();
				NameNode n = new NameNode();
				n.primaryName = d.id.name;
				a.left = n;
				a.type = AssignmentNode.ASSIGN;
				a.right = d.init.e;

				// compile the created expression.
				InterFunction func = new InterFunction();
				// add instance initializers
				func.isInit = true;
				func.isInstance = !isStatic;
				RegisterAllocator r = new RegisterAllocator();
				CompileHistory c = new CompileHistory();
				a.compile(syms, func, r, c);
				f.addFunction(func);
			}
		}
	}
}
