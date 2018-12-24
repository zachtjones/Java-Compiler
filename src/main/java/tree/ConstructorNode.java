package tree;

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
	private String fileName;
	private int line;
    
    public ConstructorNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }

	public void resolveImports(ClassLookup c) throws CompileException {
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
		// TODO, handle super(...) or this(...)   (Issue #16)
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
			if (p.isVarargs) {
				if (func.lastArgVarargs) { // can only have one argument varargs, and as to be last
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
			newTable.putEntry(p.id.name, p.type.interRep(), fileName, line);
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
		CompileHistory c = new CompileHistory();
		for (BlockStatementNode b : this.code) {
			b.compile(newTable, func);
		}
		
		// add the function to the intermediate file.
		f.addFunction(func);		
	}

	
}
