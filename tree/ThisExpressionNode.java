package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** "this" */
public class ThisExpressionNode implements Expression {

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		c.setThis();
	}

}
