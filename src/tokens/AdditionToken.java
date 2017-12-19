package tokens;

public class AdditionToken implements Token {
	private AdditionToken() {}
	
	private static AdditionToken instance = new AdditionToken();
	
	public static AdditionToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("+");
	}
}
