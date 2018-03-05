package tokens;

import declarations.statements.Expression;

public class NumberToken implements Token, Expression {
	/** Either a Double or a Long */
	Number value;
	
	public NumberToken(String token) {
		if (token.contains(".")) {
			// either double or float
			this.value = Double.parseDouble(token);
		} else {
			this.value = Long.parseLong(token);
		}
	}

	public static boolean matches(String token) {
		return token.matches("-?\\d+(\\.\\d+)?"); // regex for numbers
	}
	
	/**
	 * Multiplies this by -1.
	 */
	public void negate() {
		if (this.value instanceof Long) {
			this.value = -this.value.longValue();
		} else {
			this.value = -this.value.doubleValue();
		}
	}
}
