package tree;

import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NameNode extends NodeImpl {
	@NotNull public String primaryName;
	@Nullable private final ArrayList<GenericNode> generics;
	
    public NameNode(String fileName, int line, @NotNull String name, @Nullable ArrayList<GenericNode> generics) {
    	super(fileName, line);
    	this.primaryName = name;
    	this.generics = generics;
    }

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		primaryName = c.getFullName(primaryName);
		// don't have to check if one of primaryName or bounds is set, as this is handled by the parser
		
		// resolve the nested structures as well
		if (generics != null) {
			for (GenericNode n : generics) {
				n.resolveImports(c);
			}
		}
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
    	throw new CompileException("Name is used other than for holding type info.", getFileName(), getLine());
	}
}
