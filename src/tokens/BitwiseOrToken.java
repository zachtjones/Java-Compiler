package tokens;

public class BitwiseOrToken implements Token {
	private BitwiseOrToken() {}
	
	private static BitwiseOrToken instance = new BitwiseOrToken();
	
	public static BitwiseOrToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("|");
	}
}
