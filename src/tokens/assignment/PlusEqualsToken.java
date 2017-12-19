package tokens.assignment;

import tokens.Token;

public class PlusEqualsToken implements Token {
	private PlusEqualsToken() {}
	
	private static PlusEqualsToken instance = new PlusEqualsToken();
	
	public static PlusEqualsToken getInstance() {
		return instance;
	}
}
