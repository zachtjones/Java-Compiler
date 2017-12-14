package tokens;

import java.util.HashMap;

import declarations.ArgType;

public class KeywordToken implements Token, ArgType {
	
	// static stuff //
	private static HashMap<String, KeywordType> values;
	
	/**
	 * Sets up the hash map that is used later
	 */
	private static void setup() {
		values = new HashMap<String, KeywordType>(KeywordType.values().length);
		for (KeywordType t: KeywordType.values()) {
			values.put(t.name().toLowerCase(), t);
		}
	}
	
	/**
	 * Returns true if the token is a Keyword.
	 * @param token The string that is the token read.
	 * @return true if this token is a keyword, false otherwise.
	 */
	public static boolean matches(String token) {
		if (values == null) { // lazy creation
			setup();
		}
		
		return values.containsKey(token);
	}
	
	// instance stuff
	
	public KeywordType t;
	
	/**
	 * Constructs a Keyword instance from the String.
	 * You should check Keyword.matches(String token) first before calling this.
	 * @param token The string to convert to a Keyword instance.
	 */
	public KeywordToken(String token) {
		this.t = values.get(token);
	}
	
	@Override
	public String toString() {
		return "'Keyword: " + this.t.name() + "'";
	}
}
