package tokens;

public class ColonToken implements Token {
	private ColonToken() {}
	
	private static ColonToken instance = new ColonToken();
	
	public static ColonToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(":");
	}
}
