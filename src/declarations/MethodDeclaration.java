package declarations;

import java.util.ArrayList;

import tokens.ClassToken;

public class MethodDeclaration {
	public String name;
	public ArgType type; // return type
	public ArrayList<Parameter> params;
	public ArrayList<Statement> statements;
	public ArrayList<ClassToken> thrownClasses;
	public boolean isStatic;
	public boolean isContstructor;
}

class Parameter {
	ArgType type;
	String name;
	boolean varargs;
}
