package intermediate;

/** getLocalAddress %register = name */
public class GetParamAddressStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new put local variable statement.
	 * @param r The register to set
	 * @param localName The local variable to get.
	 */
	public GetParamAddressStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getParamAddress " + r.toString() + " = " + localName + ";";
	}
}
