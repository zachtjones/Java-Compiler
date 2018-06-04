package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

public class EnumNode implements Node {
    public String name;
    public ArrayList<String> values;
    
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		// nothing needed
	}
	
	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// nothing needed either -- only simple enum's supported.
		// this method should not be called, since you call the compile(String) one instead.
	}

	/**
	 * Generates an intermediate file for this enum node.
	 * @param packageName The package's name, or null for default package.
	 * @param classLevel The classLevel symbols (from imports)
	 * @return A new intermediate file.
	 */
	public InterFile compile(String packageName, SymbolTable classLevel) {
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "." + name);
		} else {
			f = new InterFile(name);
		}
		for (int i = 0; i < values.size(); i++) {
			String id = values.get(i);
			f.addField("int", id, true, String.valueOf(i));
		}
		return f;
	}

	
}
