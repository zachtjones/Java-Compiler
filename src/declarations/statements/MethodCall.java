package declarations.statements;

import java.util.ArrayList;

import tokens.Symbol;
import tokens.Token;

public class MethodCall implements Expression {
	/** The classToken that is the class for static method calls,
	 *  or the symbol that this method is called on.
	 *   Null for the current class's stuff*/
	public Token object; // either the ClassToken that is the class 
	//   for static ones or the object used for non-static ones
	
	public Symbol name;
	public ArrayList<Expression> args = new ArrayList<Expression>();
}
