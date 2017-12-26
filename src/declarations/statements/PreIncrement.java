package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class PreIncrement implements Statement {
	public Symbol s;
	
	public PreIncrement(Symbol s) {
		this.s = s;
	}
}
