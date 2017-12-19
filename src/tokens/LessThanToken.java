package tokens;

public class LessThanToken implements Token {
	private LessThanToken() {}
	
	private static LessThanToken instance = new LessThanToken();
	
	public static LessThanToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("<");
	}
}
