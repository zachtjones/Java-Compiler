package intermediate;

/** getLocal %register = name */
public class GetLocalStatement implements InterStatement {
	int registerNum;
	String localName;
	
	/**
	 * Creates a new put local variable statement.
	 * @param registerNum The register number to set
	 * @param localName The local variable to get.
	 */
	public GetLocalStatement(int registerNum, String localName) {
		this.registerNum = registerNum;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getLocal %" + registerNum + " = " + localName + ";";
	}
}
