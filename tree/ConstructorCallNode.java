package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.AllocateClassMemoryStatement;
import intermediate.CallVirtualStatement;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;

/** new Name (args) */
public class ConstructorCallNode implements Expression {
    public NameNode name;
    public ArgumentExpressionNode args;
    public String fileName;
    public int line;
    
    public ConstructorCallNode(String fileName, int line) {
    	this.fileName = fileName;
    	this.line = line;
    }
    
    @Override
    public String getFileName() {
    	return fileName;
    }
    
    @Override
    public int getLine() {
    	return line;
    }
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		name.resolveImports(c);
		args.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		Register result = r.getNext(name.primaryName);

		ArrayList<Expression> expressions = args.expressions;
		// compile in the args
		Register[] results = new Register[expressions.size()];
		for(int i = 0; i < expressions.size(); i++) {
			expressions.get(i).compile(s, f, r, c);
			results[i] = r.getLast();
		}
		
		// allocate memory
		f.statements.add(new AllocateClassMemoryStatement(name.primaryName, result));
		
		// copy
		Register finalResult = r.getNext(result.type);
		
		// add in the call virtual statement
		f.statements.add(new CallVirtualStatement(result, "<init>", results, null, fileName, line));
		
		// result is the finalResult
		f.statements.add(new CopyStatement(result, finalResult, fileName, line));
		
	}
}
