package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This represents a class in the tree
* @author Zach Jones
*/
public class ClassNode implements TypeDecNode {
	@NotNull private final String fileName;
	private final int line;

	private final boolean isAbstract;
    private final boolean isFinal;
    private final boolean isPublic;
    @NotNull private final String name;
	@Nullable public ArrayList<NameNode> typeParams;

	@NotNull private final NameNode superclass; // used if a class, or even abstract class.
	@NotNull private final ArrayList<NameNode> interfaces;
	@NotNull public ArrayList<ClassBodyNode> body;

    public ClassNode(@NotNull String fileName, int line, boolean isAbstract, boolean isFinal, boolean isPublic,
					 @NotNull String name, @Nullable ArrayList<NameNode> typeParams, @Nullable NameNode superClass,
					 @Nullable ArrayList<NameNode> implementedInterfaces, @NotNull ArrayList<ClassBodyNode> body) {

    	this.fileName = fileName;
    	this.line = line;
    	this.isAbstract = isAbstract;
    	this.isFinal = isFinal;
    	this.isPublic = isPublic;
    	this.name = name;
    	this.typeParams = typeParams;
    	final NameNode superName = new NameNode(fileName, line);
    	superName.primaryName = "java/lang/Object";

    	this.superclass = superClass == null ? superName : superClass;
    	this.interfaces = implementedInterfaces == null ? new ArrayList<>() : implementedInterfaces;
    	this.body = body;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		superclass.resolveImports(c);
		for (NameNode n : interfaces) {
			n.resolveImports(c);
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
	public InterFile compile(@Nullable String packageName, @NotNull SymbolTable classLevel) throws CompileException {
		ArrayList<String> extendsNames = new ArrayList<>();
    	interfaces.forEach(i -> extendsNames.add(i.primaryName));
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "/" + name, superclass.primaryName, extendsNames);
		} else {
			f = new InterFile(name, superclass.primaryName, extendsNames);
		}
		
		// place the class name in the symbol table (used for static fields)
		classLevel.putEntry(name, Types.CLASS, fileName, line);

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
		
		if (!isAbstract) {
			// generate default <init> if needed only.
			f.generateDefaultConstructor(fileName, line);
		}
		
		// TODO stuff with the isAbstract, isFinal optimizations
		
		return f;
	}

	
}
