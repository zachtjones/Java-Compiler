package declarations.statements;

import java.util.ArrayList;

import tokens.ClassToken;

public class ArrayInitializerSize implements Expression {
	public ArrayList<Expression> bounds = new ArrayList<Expression>();
	public ClassToken type;
}
