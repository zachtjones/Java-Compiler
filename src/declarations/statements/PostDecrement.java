package declarations.statements;

import tokens.Symbol;

public class PostDecrement implements Expression {
	public Symbol s;
	
	public PostDecrement(Symbol s) {
		this.s = s;
	}
}
