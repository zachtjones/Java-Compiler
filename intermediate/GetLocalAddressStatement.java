package intermediate;

public class GetLocalAddressStatement implements InterStatement {
	Register r;
	String localName;
	
	/**
	 * Creates a new get local variable address statement.
	 * @param r The register to set containing the address
	 * @param localName The local variable to get.
	 */
	public GetLocalAddressStatement(Register r, String localName) {
		this.r = r;
		this.localName = localName;
	}
	
	@Override
	public String toString() {
		return "getLocalAddress " + r.toString() + " = " + localName + ";";
	}
}
