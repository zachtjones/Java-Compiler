package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class PlusEqualsStatement implements Statement {
	public Symbol s;
	public Expression e;
	
	public PlusEqualsStatement(Symbol s, Expression e) {
		this.s = s;
		this.e = e;
	}
}
