package tokens.assignment;

import tokens.Token;

public class BitOrEqualsToken implements Token {
	private BitOrEqualsToken() {}
	
	private static BitOrEqualsToken instance = new BitOrEqualsToken();
	
	public static BitOrEqualsToken getInstance() {
		return instance;
	}
}
