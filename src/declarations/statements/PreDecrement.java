package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class PreDecrement implements Statement {
	public Symbol s;
	
	public PreDecrement(Symbol s) {
		this.s = s;
	}
}
