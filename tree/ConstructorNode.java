package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.InterStatement;

public class ConstructorNode implements Node {
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

	@Override
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
	
	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		// add in the parameters
		SymbolTable newTable = new SymbolTable(s, SymbolTable.parameter);
		for (ParamNode p : params) {
			newTable.putEntry(p.id.name, p.type.interRep());
		}
		// pass down to the code with the parameters in the table
		for (BlockStatementNode b : code) {
			b.resolveSymbols(newTable);
		}
	}

	/**
	 * Compiles this constructor into the intermediate file.
	 * @param f The intermediate file to compile into.
	 * @throws CompileException If there is a compiling error.
	 */
	public void compile(InterFile f) throws CompileException {
		// name is <init>
		InterFunction func = new InterFunction();
		func.name = "<init>";
		// returns object name
		func.returnType = name;
		
		// add the parameters
		for (ParamNode p : this.params) {
			func.paramTypes.add(p.type.interRep());
		}
		
		// throws the throws list
		for (NameNode n : this.throwsList) {
			func.throwsList.add(n.primaryName);
		}
		
		// add in the implicit super() call
		// TODO func.statements.add(new );
		
		// compile the block
		for (BlockStatementNode b : this.code) {
			ArrayList<InterStatement> stmt = b.compile();
			for (InterStatement i : stmt) {
				func.statements.add(i);
			}
		}
		
		// add the function
		f.addFunction(func);		
	}

	
}
