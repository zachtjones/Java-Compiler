package tokens;

public class BitwiseXOrToken implements Token {
	private BitwiseXOrToken() {}
	
	private static BitwiseXOrToken instance = new BitwiseXOrToken();
	
	public static BitwiseXOrToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("^");
	}
}
