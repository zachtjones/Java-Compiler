package declarations.statements;

import java.util.ArrayList;

import tokens.ClassToken;

public class ConstructorCall implements Expression {
	public ClassToken classToken; // the class token that is the constructor's type
	public ArrayList<Expression> parameters = new ArrayList<Expression>();
	
}
