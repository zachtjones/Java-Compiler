package tokens;

public class RightCurlyBrace implements Token {
	private RightCurlyBrace() {}
	
	// all LeftCurlyBraces are the same -- no need for official contructor
	private static RightCurlyBrace instance = new RightCurlyBrace();
	
	public static RightCurlyBrace getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("}");
	}
}
