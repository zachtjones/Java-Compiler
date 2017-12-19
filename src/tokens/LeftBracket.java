package tokens;

public class LeftBracket implements Token {
	private LeftBracket() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static LeftBracket instance = new LeftBracket();
	
	public static LeftBracket getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("[");
	}
}
