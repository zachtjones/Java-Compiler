package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterStatement;

public class FieldDeclarationNode implements Node {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;
    public boolean isStatic;
    public boolean isFinal;
    public boolean isTransient;
    public boolean isVolatile;

    public TypeNode type;
    public ArrayList<VariableDecNode> variables = new ArrayList<>();
    
	@Override
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
			if (d.init.e != null) {
				// construct the assignment to the expression
				ExpressionNode e1 = new ExpressionNode();
				e1.assignType = AssignmentOperator.ASSIGN;
				ExpressionNode id = new ExpressionNode();
				id.name = new NameNode();
				id.name.primaryName = d.id.name;
				e1.op1 = id;
				e1.op2 = d.init.e;
				
				// compile the created expression.
				ArrayList<InterStatement> compiled = e1.compile();
				for (InterStatement s : compiled) {
					f.addInstanceInit(s);
				}
			} else {
				// it's an array initializer (possibly nested)
				throw new CompileException("Array init not implemented yet.");
			}
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// put the new symbols into the table
		for (VariableDecNode v : variables) {
			s.putEntry(v.id.name, type.interRep());
		}
	}
}
