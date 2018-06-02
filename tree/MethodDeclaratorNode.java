package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class MethodDeclaratorNode implements Node {
    public String name;
    public ArrayList<ParamNode> params;
    public int arrayDims; // 0 for non array
    // arrayDims is for the return type
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		for (ParamNode p : params) {
			p.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// set the name
		f.name = name;
		
		// s is for the parameters, place them in s
		for (ParamNode p : params) {
			s.putEntry(p.id.name, p.type.interRep());
			f.paramTypes.add(p.type.interRep());
			f.paramNames.add(p.id.name);
		}
	}
}
