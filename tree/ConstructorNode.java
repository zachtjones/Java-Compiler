package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class ConstructorNode {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;

    public String name;

    public ArrayList<ParamNode> params; // empty at worst
    public ArrayList<NameNode> throwsList; // could be null

    // constructor call - either this(...) or super(...)
    //  could be null
    //public ConstructorCallNode cc;
    
    // the statements that make up the rest of the block
    public ArrayList<BlockStatementNode> code = new ArrayList<>();

	public void resolveImports(ClassLookup c) throws IOException {
		for (ParamNode p : params) {
			p.resolveImports(c);
		}
		if (throwsList != null) {
			for (NameNode n : throwsList) {
				n.resolveImports(c);
			}
		}
		for (BlockStatementNode b : code) {
			b.resolveImports(c);
		}
	}

	/**
	 * Compiles this constructor into the intermediate file.
	 * @param f The intermediate file to compile into.
	 * @param s The symbol table for the class.
	 * @throws CompileException If there is a compiling error.
	 */
	public void compile(InterFile f, SymbolTable s) throws CompileException {
		System.out.println("Should handle parsing of Explicit super() or this() in constructor declarations.");
		// name is <init>
		InterFunction func = new InterFunction();
		func.name = "<init>";
		func.isInstance = true;
		// returns object name
		func.returnType = name;
		
		// symbol table for parameters
		SymbolTable newTable = new SymbolTable(s, SymbolTable.parameter);
		RegisterAllocator r = new RegisterAllocator();
		
		// add the parameters
		for (ParamNode p : this.params) {
			func.paramTypes.add(p.type.interRep());
			func.paramNames.add(p.id.name);
			newTable.putEntry(p.id.name, p.type.interRep());
		}
		
		// throws the throws list
		if (this.throwsList != null) {
			for (NameNode n : this.throwsList) {
				func.throwsList.add(n.primaryName);
			}
		}
		// add in the implicit super() call
		// TODO func.statements.add(new );
		
		// compile the block
		for (BlockStatementNode b : this.code) {
			b.compile(newTable, func, r, c);
		}
		
		// add the function to the intermediate file.
		f.addFunction(func);		
	}

	
}
