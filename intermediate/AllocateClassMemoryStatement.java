package intermediate;

public class AllocateClassMemoryStatement implements InterStatement {

	String type;
	Register result;

	/**
	 * Represents an allocation of memory for a class instance.
	 * @param type The fully-qualified class name.
	 * @param result The register to hold a reference to the memory allocated.
	 */
	public AllocateClassMemoryStatement(String type, Register result) {
		this.type = type;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "allocateType " + result + " = " +  type + ";";
	}

}
