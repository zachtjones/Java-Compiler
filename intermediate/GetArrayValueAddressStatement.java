package intermediate;

import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

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

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		UsageCheck.verifyDefined(array, regs);
		UsageCheck.verifyDefined(index, regs);
		
		// make sure the type of the array ends in []
		if (!array.typeFull.endsWith("[]")) {
			throw new CompileException("can't index type: " + array.typeFull);
		}

		String resulting = array.typeFull.substring(0, array.typeFull.length() - 2);
		result.typeFull = resulting + "*"; // pointer to the value

		regs.put(result, result.typeFull);
	}
}
