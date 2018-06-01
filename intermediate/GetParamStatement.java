package intermediate;

/** getParam %register = name */
public class GetParamStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new get parameter variable statement.
	 * @param registerNum The register number to set
	 * @param localName The parameter to get.
	 */
	public GetParamStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getParam %" + r.toString() + " = " + localName + ";";
	}
}
