package tokens;

public class DotToken implements Token {
	private DotToken() {}
	
	private static DotToken instance = new DotToken();
	
	public static DotToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals(".");
	}
}
