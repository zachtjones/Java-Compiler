package declarations.statements;

import tokens.Symbol;

public class PropertyAccess implements Expression {
	public Symbol left;
	public Symbol right;
	
	/** Creates a property access from the form: <code>left.right</code>*/
	public PropertyAccess(Symbol left, Symbol right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	
}
