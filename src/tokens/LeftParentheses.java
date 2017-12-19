package tokens;

public class LeftParentheses implements Token {
	private LeftParentheses() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static LeftParentheses instance = new LeftParentheses();
	
	public static LeftParentheses getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("(");
	}
}
