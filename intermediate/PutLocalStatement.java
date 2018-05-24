package intermediate;

/** PutLocal name = %register */
public class PutLocalStatement implements InterStatement {
	int registerNum;
	String localName;
	
	/**
	 * Creates a new put local variable statement.
	 * @param registerNum The register number to grab
	 * @param localName The local variable to set.
	 */
	public PutLocalStatement(int registerNum, String localName) {
		this.registerNum = registerNum;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "PutLocal " + localName + " = %r" + registerNum + ";";
	}
}
