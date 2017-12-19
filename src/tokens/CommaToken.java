package tokens;

public class CommaToken implements Token {
	private CommaToken() {}
	
	private static CommaToken instance = new CommaToken();
	
	public static CommaToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(",");
	}
}
