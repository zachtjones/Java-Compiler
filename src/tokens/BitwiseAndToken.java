package tokens;

public class BitwiseAndToken implements Token {
	private BitwiseAndToken() {}
	
	private static BitwiseAndToken instance = new BitwiseAndToken();
	
	public static BitwiseAndToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("&");
	}
}
