package tokens;

public class RightBracket implements Token {
	private RightBracket() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static RightBracket instance = new RightBracket();
	
	public static RightBracket getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("]");
	}
}
