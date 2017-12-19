package tokens;

public class DivideToken implements Token {
	private DivideToken() {}
	
	private static DivideToken instance = new DivideToken();
	
	public static DivideToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("/");
	}
}
