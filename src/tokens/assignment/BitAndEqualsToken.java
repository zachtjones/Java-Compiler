package tokens.assignment;

import tokens.Token;

public class BitAndEqualsToken implements Token {
	private BitAndEqualsToken() {}
	
	private static BitAndEqualsToken instance = new BitAndEqualsToken();
	
	public static BitAndEqualsToken getInstance() {
		return instance;
	}
}
