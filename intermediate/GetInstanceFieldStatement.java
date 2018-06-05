package intermediate;

public class GetInstanceFieldStatement implements InterStatement {
	Register instance;
	String fieldName;
	
	Register result;
	
	/**
	 * Creates a new get static field statement. 
	 * @param instance The instance's register holding it's value.
	 * @param fieldName The field's name.
	 */
	public GetInstanceFieldStatement(Register instance, String fieldName, Register result) {
		this.instance = instance;
		this.fieldName = fieldName;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "getInstanceField " + fieldName + " of " + instance.toString() 
			+ " to " + result.toString() + ";";
	}
}
