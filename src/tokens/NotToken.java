package tokens;

public class NotToken implements Token {
	private NotToken() {}
	
	private static NotToken instance = new NotToken();
	
	public static NotToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("!");
	}
}
