package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

/** Represents a do-nothing operation*/
public class NoOp implements Node, Expression {

	@Override
	public void resolveImports(ClassLookup c) throws IOException {	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, 
			CompileHistory c) throws CompileException {}

}
