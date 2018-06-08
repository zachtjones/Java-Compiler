package intermediate;

public class GetArrayValueAddressStatement implements InterStatement {
	
	Register array, index, result;

	/**
	 * Represents getting a value's address out of an array.
	 */
	public GetArrayValueAddressStatement(Register array, Register index, Register result) {
		this.array = array;
		this.index = index;
		this.result = result;
	}

	public String toString() {
		return "getArrayValue " + result + " = " + array + " @ " + index + ";";
	}
}
