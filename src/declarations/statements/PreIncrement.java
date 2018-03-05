package declarations.statements;

import tokens.Symbol;

public class PreIncrement implements Expression {
	public Symbol s;
	
	public PreIncrement(Symbol s) {
		this.s = s;
	}
}
