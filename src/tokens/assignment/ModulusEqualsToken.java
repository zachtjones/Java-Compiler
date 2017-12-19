package tokens.assignment;

import tokens.Token;

public class ModulusEqualsToken implements Token {
	private ModulusEqualsToken() {}
	
	private static ModulusEqualsToken instance = new ModulusEqualsToken();
	
	public static ModulusEqualsToken getInstance() {
		return instance;
	}
}
