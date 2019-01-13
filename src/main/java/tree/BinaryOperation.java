package tree;

/** These are the non short-circuiting binary operations (+ - / * % ^ & &lt;&lt; &gt;&gt; &gt;&gt;&gt;) */
public enum BinaryOperation {
	ADD('+'), AND('&');

	private final char rep;
	BinaryOperation(char rep) {
		this.rep = rep;
	}

	/** returns the java representation of this binary operation */
	public char getRepresentation() {
		return rep;
	}
}
