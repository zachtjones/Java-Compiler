package tokens;

public class LeftCurlyBrace implements Token {
	private LeftCurlyBrace() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static LeftCurlyBrace instance = new LeftCurlyBrace();
	
	public static LeftCurlyBrace getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("{");
	}
}
