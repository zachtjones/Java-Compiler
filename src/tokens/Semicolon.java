package tokens;

public class Semicolon implements Token {
	private Semicolon() {}
	
	// all LeftCurlyBraces are the same -- no need for official constructor
	private static Semicolon instance = new Semicolon();
	
	public static Semicolon getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(";");
	}
}
