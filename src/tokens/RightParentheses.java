package tokens;

public class RightParentheses implements Token {
	private RightParentheses() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static RightParentheses instance = new RightParentheses();
	
	public static RightParentheses getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(")");
	}
}
