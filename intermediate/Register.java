package intermediate;

public class Register {
	
	public static final int BYTE = 0;
	public static final int SHORT = 1;
	public static final int INT = 2;
	public static final int LONG = 3;
	public static final int FLOAT = 4;
	public static final int DOUBLE = 5;
	public static final int REFERENCE = 6;
	
	public static final int LABEL = 7;
	
	public int num;
	public int type;
	
	public Register(int num, int type) {
		this.num = num;
		this.type = type;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case BYTE: return "%b" + num;
		case SHORT: return "%s" + num;
		case INT: return "%i" + num;
		case LONG: return "%l" + num;
		case FLOAT: return "%f" + num;
		case DOUBLE: return "%d" + num;
		case REFERENCE: return "%r" + num;
		}
		return "unknown register type: " + type;
	}

	/**
	 * Gets the larger type (the one with the most width)
	 * Ex: float & long = float;
	 * @param first The Register constant type of the first item.
	 * @param second The Register constant type of the second item.
	 * @return The Register constant type of the result.
	 */
	public static int getLarger(int first, int second) {
		if (first == second) return first;
		if (first == DOUBLE || second == DOUBLE) return DOUBLE;
		if (first == FLOAT || second == FLOAT) return FLOAT;
		if (first == LONG || second == LONG) return LONG;
		if (first == INT || second == INT) return INT;
		if (first == SHORT || second == SHORT) return SHORT;
		return BYTE;
	}
}
