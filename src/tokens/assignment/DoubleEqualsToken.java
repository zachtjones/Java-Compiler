package tokens.assignment;

import tokens.Token;

public class DoubleEqualsToken implements Token {
	private DoubleEqualsToken() {}
	
	private static DoubleEqualsToken instance = new DoubleEqualsToken();
	
	public static DoubleEqualsToken getInstance() {
		return instance;
	}
}
