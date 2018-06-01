package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.LoadLiteralStatement;
import intermediate.RegisterAllocator;

public class LiteralExpressionNode implements Expression {
	
    public String value;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing to do
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// the IL code does the work here
		f.statements.add(new LoadLiteralStatement(value, r));	
	}
}
