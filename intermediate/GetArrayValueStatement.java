package intermediate;

public class GetArrayValueStatement implements InterStatement {
	
	Register array, index, result;

	/**
	 * Represents getting a value out of an array.
	 */
	public GetArrayValueStatement(Register array, Register index, Register result) {
		this.array = array;
		this.index = index;
		this.result = result;
	}

	public String toString() {
		return "getArrayValue " + result + " = " + array + " @ " + index + ";";
	}
}
