package intermediate;

public class GetThisFieldAddressStatement implements InterStatement {
	Register r;
	String fieldName;
	
	/**
	 * Creates a new get field variable address statement.
	 * @param registerNum The register number to set
	 * @param fieldName The field to get.
	 */
	public GetThisFieldAddressStatement(Register r, String fieldName) {
		this.r = r;
		this.fieldName = fieldName;
	}
	
	@Override
	public String toString() {
		return "getThisFieldAddress " + r.toString() + " = " + fieldName + ";";
	}
}
