package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import intermediate.InterFunction;
import org.jetbrains.annotations.NotNull;

/** This represents a class in the tree
* @author Zach Jones
*/
public class ClassNode extends NodeImpl implements TypeDecNode {
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
    
    public ClassNode(String fileName, int line) {
    	super(fileName, line);
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		if (superclass != null) {
			superclass.resolveImports(c);
		} else {
			superclass = new NameNode(getFileName(), getLine());
			superclass.primaryName = "java/lang/Object";
		}
		if (interfaces != null) {
			for (NameNode n : interfaces) {
				n.resolveImports(c);
			}	
		} else {
			interfaces = new ArrayList<>();
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
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// this method should not be called.
	}

	public InterFile compile(String packageName, SymbolTable classLevel) throws CompileException {
    	ArrayList<NameNode> extendsNodes = isInterface ? supers : interfaces;
    	ArrayList<String> extendsNames = new ArrayList<>();
    	extendsNodes.forEach(i -> extendsNames.add(i.primaryName));
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "/" + name, superclass.primaryName, extendsNames);
		} else {
			f = new InterFile(name, superclass.primaryName, extendsNames);
		}
		
		// place the class name in the symbol table (used for static fields)
		classLevel.putEntry(name, Types.CLASS, getFileName(), getLine());

		// put the symbols for this class into the symbol table
		SymbolTable staticFields = new SymbolTable(classLevel, SymbolTable.staticFields);
		SymbolTable instanceFields = new SymbolTable(staticFields, SymbolTable.instanceFields);
		for (ClassBodyNode c : body) {
			c.putSymbols(classLevel, staticFields, instanceFields);
		}
		
		// fields / methods -- each one gets new register allocator and function.
		SymbolTable syms = new SymbolTable(instanceFields, SymbolTable.className);
		for (ClassBodyNode c : body) {
			c.compile(f, syms);
		}
		
		if (!isAbstract && !isInterface) {
			// generate default <init> if needed only.
			f.generateDefaultConstructor(getFileName(), getLine());
		}
		
		// TODO stuff with the isAbstract, isFinal optimizations
		
		return f;
	}

	
}
