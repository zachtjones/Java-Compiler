package tokens.assignment;

import tokens.Token;

public class DivideEqualsToken implements Token {
	private DivideEqualsToken() {}
	
	private static DivideEqualsToken instance = new DivideEqualsToken();
	
	public static DivideEqualsToken getInstance() {
		return instance;
	}
}
