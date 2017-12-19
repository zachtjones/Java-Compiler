package tokens;

public class GreaterThanToken implements Token {
	private GreaterThanToken() {}
	
	private static GreaterThanToken instance = new GreaterThanToken();
	
	public static GreaterThanToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(">");
	}
}
