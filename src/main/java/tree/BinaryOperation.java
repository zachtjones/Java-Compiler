package tree;

/** These are the non short-circuiting binary operations (+ - / * % ^ & &lt;&lt; &gt;&gt; &gt;&gt;&gt;) */
public enum BinaryOperation {
	ADD("+"), SUBTRACT("-"),
	TIMES("*"), DIVIDE("/"), MOD("%"),
	AND("&"), XOR("^"), OR("|"),
	LEFT_SHIFT("<<"), RIGHT_SHIFT_SIGN(">>"), RIGHT_SHIFT_UNSIGNED(">>>");

	private final String rep;
	BinaryOperation(String rep) {
		this.rep = rep;
	}

	/** returns the java representation of this binary operation */
	public String getRepresentation() {
		return rep;
	}
}
