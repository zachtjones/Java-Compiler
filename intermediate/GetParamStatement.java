package intermediate;

/** getParam %register = name */
public class GetParamStatement implements InterStatement {
	int registerNum;
	String localName;
	
	/**
	 * Creates a new put local variable statement.
	 * @param registerNum The register number to set
	 * @param localName The parameter to get.
	 */
	public GetParamStatement(int registerNum, String localName) {
		this.registerNum = registerNum;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getParam %" + registerNum + " = " + localName + ";";
	}
}
