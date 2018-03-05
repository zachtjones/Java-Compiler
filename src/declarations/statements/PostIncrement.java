package declarations.statements;

import tokens.Symbol;

public class PostIncrement implements Expression {
	public Symbol s;
	
	public PostIncrement(Symbol s) {
		this.s = s;
	}
}
