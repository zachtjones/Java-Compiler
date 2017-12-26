package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class PostIncrement implements Statement {
	public Symbol s;
	
	public PostIncrement(Symbol s) {
		this.s = s;
	}
}
