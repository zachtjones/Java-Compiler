package tokens;

public class BitNotToken implements Token {
	private BitNotToken() {}
	
	private static BitNotToken instance = new BitNotToken();
	
	public static BitNotToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("~");
	}
}
