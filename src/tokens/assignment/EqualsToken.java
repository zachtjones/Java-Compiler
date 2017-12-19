package tokens.assignment;

import tokens.Token;

public class EqualsToken implements Token {
	private EqualsToken() {}
	
	private static EqualsToken instance = new EqualsToken();
	
	public static EqualsToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("=");
	}
}
