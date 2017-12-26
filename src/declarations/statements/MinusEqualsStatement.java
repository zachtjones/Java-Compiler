package declarations.statements;

import declarations.Statement;
import tokens.Symbol;

public class MinusEqualsStatement implements Statement {
	public Symbol s;
	public Expression e;
	
	public MinusEqualsStatement(Symbol s, Expression e) {
		this.s = s;
		this.e = e;
	}
}
