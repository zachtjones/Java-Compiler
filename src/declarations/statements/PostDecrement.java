package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class PostDecrement implements Statement {
	public Symbol s;
	
	public PostDecrement(Symbol s) {
		this.s = s;
	}
}
