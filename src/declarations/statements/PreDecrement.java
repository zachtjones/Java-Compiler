package declarations.statements;

import tokens.Symbol;

public class PreDecrement implements Expression {
	public Symbol s;
	
	public PreDecrement(Symbol s) {
		this.s = s;
	}
}
