package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;

/** This represents a class in the tree
* @author Zach Jones
*/
public class ClassNode implements Node {
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isPublic;
    public boolean isInterface;
    public String name;
    
    public NameNode superclass; // used if a class, or even abstract class.
    public ArrayList<NameNode> supers; // used if an interface
    public ArrayList<NameNode> interfaces;
    public ArrayList<ClassBodyNode> body = new ArrayList<>();
    public ArrayList<NameNode> typeParams;

	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		if (superclass != null) {
			superclass.resolveImports(c);
		}
		if (interfaces != null) {
			for (NameNode n : interfaces) {
				n.resolveImports(c);
			}	
		}
		for (ClassBodyNode b : body) {
			b.resolveImports(c);
		}
		if (typeParams != null) {
			for (NameNode n : typeParams) {
				n.resolveImports(c);
			}
		}
		
	}
	
	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		this.body.sort((ClassBodyNode c1, ClassBodyNode c2) -> {
			if (c1.field != null) return -1; // put fields first
			return 0;
		});
		for (ClassBodyNode c : body) {
			c.resolveSymbols(s);
		}
	}

	public InterFile compile(String packageName) throws CompileException {
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "." + name);
		} else {
			f = new InterFile(name);
		}
		
		// define the super classes and interfaces
		//   - treated the same way (kind of)
		if (this.isInterface) {
			f.setImplements(this.supers);
		} else {
			f.setExtends(this.superclass);
			f.setImplements(this.interfaces);
			if (!isAbstract) {
				// potentially generate default constructor
				System.out.println("Should generate default constructor.");
			}
		}
		
		// place fields first -- used in symbol table
		body.sort((ClassBodyNode e1, ClassBodyNode e2) -> {
			if (e1.field != null) { 
				return -1;
			}
			return 0;
		});
		
		// fields / methods
		SymbolTable syms = new SymbolTable(null, SymbolTable.className);
		for (ClassBodyNode c : body) {
			c.compile(f, syms);
		}
		
		// TODO stuff with the isAbstract, isFinal optimizations
		
		return f;
	}

	
}
