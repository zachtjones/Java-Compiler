package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFile;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

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
    public String fileName;
    public int line;
    
    public ClassNode(String fileName, int line) {
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
	public void resolveImports(ClassLookup c) throws CompileException {
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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		// this method should not be called.
	}

	public InterFile compile(String packageName, SymbolTable classLevel) throws CompileException {
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "/" + name);
		} else {
			f = new InterFile(name);
		}
		
		// place the class name in the symbol table (used for static fields)
		classLevel.putEntry(name, "className", fileName, line);
		
		// define the super classes and interfaces
		//   - treated the same way (kind of)
		if (this.isInterface) {
			f.setImplements(this.supers);
		} else {
			f.setExtends(this.superclass);
			f.setImplements(this.interfaces);
			
		}
		
		// place fields first -- used in symbol table
		body.sort((ClassBodyNode e1, ClassBodyNode e2) -> {
			if (e1.field != null) { 
				return -1;
			}
			return 0;
		});
		
		// fields / methods -- each one gets new register allocator and function.
		SymbolTable syms = new SymbolTable(classLevel, SymbolTable.className);
		for (ClassBodyNode c : body) {
			c.compile(f, syms);
		}
		
		if (!isAbstract && !isInterface) {
			// generate default <init> if needed only.
			f.generateDefaultConstructor(fileName, line);
		}
		
		// TODO stuff with the isAbstract, isFinal optimizations
		
		return f;
	}

	
}
