package tokens;

public class QuestionToken implements Token {
	private QuestionToken() {}
	
	private static QuestionToken instance = new QuestionToken();
	
	public static QuestionToken getInstance() {
		return instance;
	}
	
	public static boolean matches(String token) {
		return token.equals("?");
	}
}
