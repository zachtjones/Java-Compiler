package tokens.assignment;

import tokens.Token;

public class BitXorEqualsToken implements Token {
	private BitXorEqualsToken() {}
	
	private static BitXorEqualsToken instance = new BitXorEqualsToken();
	
	public static BitXorEqualsToken getInstance() {
		return instance;
	}
}
