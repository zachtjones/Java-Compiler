package tokens;

public class ModulusToken implements Token {
	private ModulusToken() {}
	
	private static ModulusToken instance = new ModulusToken();
	
	public static ModulusToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("%");
	}
}
