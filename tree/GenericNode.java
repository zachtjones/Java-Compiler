package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class GenericNode implements Node {
	
	private final int line;
	private final String fileName;
	
	// one of these three groups will be set (not null)
	
	/** These are used for Name() [extends Name()] in the type args.
	 * The second is optional, so it could just be Name() */
	public NameNode extends1, extends2;
	
	/** Represents ? extends Name(), where Name is a series of names separated by '&'  */
	public ArrayList<NameNode> hookExtends;

	/** Represents ? super Name(), where Name is a series of names separated by '&'  */
	public ArrayList<NameNode> hookSuper;
	
	public GenericNode(String fileName, int line) {
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		if (hookSuper != null) {
			for (NameNode n : hookSuper)
				n.resolveImports(c);
		} else if (hookExtends != null) {
			for (NameNode n : hookExtends)
				n.resolveImports(c);
		} else {
			extends1.resolveImports(c);
			if (extends2 != null)
				extends2.resolveImports(c);
		}
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public int getLine() {
		return line;
	}

}
