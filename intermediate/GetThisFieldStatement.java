package intermediate;

public class GetThisFieldStatement implements InterStatement {
	Register r;
	String fieldName;
	
	/**
	 * Creates a new get field variable statement.
	 * @param registerNum The register number to set
	 * @param fieldName The field to get.
	 */
	public GetThisFieldStatement(Register r, String fieldName) {
		this.r = r;
		this.fieldName = fieldName;
	}
	
	@Override
	public String toString() {
		return "getThisField %" + r.toString() + " = " + fieldName + ";";
	}
}
