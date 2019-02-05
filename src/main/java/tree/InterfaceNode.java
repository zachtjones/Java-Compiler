package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/** This represents a class in the tree
* @author Zach Jones
*/
public class InterfaceNode implements TypeDecNode {
	@NotNull private final String fileName;
	private final int line;

	// interfaces are public and abstract by default

    @NotNull private final String name;

	@NotNull private final ArrayList<NameNode> interfaces;
	@NotNull private final ArrayList<ClassBodyNode> body;

    public InterfaceNode(@NotNull String fileName, int line, @NotNull String name,
						 @Nullable ArrayList<NameNode> implementedInterfaces,
						 @NotNull ArrayList<ClassBodyNode> body) {

    	this.fileName = fileName;
    	this.line = line;
    	this.name = name;
    	this.interfaces = implementedInterfaces == null ? new ArrayList<>() : implementedInterfaces;
    	this.body = body;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		for (NameNode n : interfaces) {
			n.resolveImports(c);
		}
		for (ClassBodyNode b : body) {
			b.resolveImports(c);
		}
	}

	@Override
	public InterFile compile(@Nullable String packageName, @NotNull SymbolTable classLevel) throws CompileException {
		throw new CompileException("InterfaceNode compiling not done yet", "", -1);
	}

	
}
