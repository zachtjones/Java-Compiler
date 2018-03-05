package tokens;

import declarations.statements.Expression;

public class Symbol implements Token, Expression {
	public String contents;
	
	public Symbol(String contents) {
		this.contents = contents; 
	}
	
	public String toString() {
		return "Symbol: " + contents;
	}
}
