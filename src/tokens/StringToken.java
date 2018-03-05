package tokens;

import java.util.StringTokenizer;

import declarations.statements.Expression;

public class StringToken implements Token, Expression {
	
	public String contents = ""; // converted string result
	
	public StringToken(StringTokenizer st) {
		// if the string ends in \", it's not the end of the string
		String lastToken = "";
		while (true) {
			String temp = st.nextToken();
			if (temp.equals("\"") && !lastToken.endsWith("\\")) {
				break; // end of string reached
			}
			contents += temp;
			lastToken = temp;
		}
	}
	
	@Override
	public String toString() {
		return "A String literal='" + this.contents + "'";
	}
	
	/**
	 * Returns true if start is the first part of a string literal
	 */
	public static boolean matches(String start) {
		return start.equals("\"");
	}
}
