package intermediate;

import java.util.HashMap;

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

	@Override
	public void typeCheck(HashMap<Register, String> regs, 
			HashMap<String, String> locals, HashMap<String, String> params, InterFunction func) {
		
		result.type = Register.REFERENCE;
		result.typeFull = type;
		regs.put(result, type);
	}

}
