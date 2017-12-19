package tokens;

public class MinusToken implements Token {
	private MinusToken() {}
	
	private static MinusToken instance = new MinusToken();
	
	public static MinusToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("-");
	}
}
