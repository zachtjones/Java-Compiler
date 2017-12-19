package tokens.assignment;

import tokens.Token;

public class LeftShiftEqualToken implements Token {
	private LeftShiftEqualToken() {}
	
	private static LeftShiftEqualToken instance = new LeftShiftEqualToken();
	
	public static LeftShiftEqualToken getInstance() {
		return instance;
	}
}
