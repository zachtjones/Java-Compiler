package tokens.assignment;

import tokens.Token;

public class MinusEqualsToken implements Token {
	private MinusEqualsToken() {}
	
	private static MinusEqualsToken instance = new MinusEqualsToken();
	
	public static MinusEqualsToken getInstance() {
		return instance;
	}
}
