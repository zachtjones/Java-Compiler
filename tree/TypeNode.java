package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class TypeNode implements Node {
    // one of these two will not be null
    public PrimitiveTypeNode primitive;
    public NameNode name;

    public int arrayDims; // 0 for not array;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (name != null) {
			name.resolveImports(c);
		}
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		// this is already done in higher nodes.
	}
	
	/**
	 * Gets the IL representation of this type.
	 */
	public String interRep() {
		if (primitive != null) {
			return primitive.interRep();
		}
		return "reference(" + name.primaryName + ")";
	}

	
}
