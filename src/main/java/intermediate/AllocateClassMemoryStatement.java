package intermediate;

import helper.Types;

import java.util.HashMap;

public class AllocateClassMemoryStatement implements InterStatement {

	private final Types type;
	private final Register result;

	/**
	 * Represents an allocation of memory for a class instance.
	 * @param type The fully-qualified class name.
	 * @param result The register to hold a reference to the memory allocated.
	 */
	public AllocateClassMemoryStatement(Types type, Register result) {
		this.type = type;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "allocateType " + result + " = " +  type + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs,
			HashMap<String, Types> locals, HashMap<String, Types> params, InterFunction func) {
		
		result.setType(type);
		regs.put(result, type);
	}

}
