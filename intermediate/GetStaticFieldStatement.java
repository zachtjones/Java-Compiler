package intermediate;

public class GetStaticFieldStatement implements InterStatement {
	String className;
	String fieldName;
	Register result;
	
	/**
	 * Creates a new get static field statement. 
	 * @param className The class's name.
	 * @param fieldName The field's name.
	 * @param register The result register.
	 */
	public GetStaticFieldStatement(String className, String fieldName, Register register) {
		this.className = className;
		this.fieldName = fieldName;
		this.result = register;
	}
	
	@Override
	public String toString() {
		return "getStaticField " + fieldName + " of " + className
				+ " to " + result.toString() + ";";
	}
}
