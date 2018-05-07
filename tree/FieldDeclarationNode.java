package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class FieldDeclarationNode implements Node {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;
    public boolean isStatic;
    public boolean isFinal;
    public boolean isTransient;
    public boolean isVolatile;

    public TypeNode type;
    public ArrayList<VariableDecNode> variables = new ArrayList<>();
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		type.resolveNames(c);
	}
}
