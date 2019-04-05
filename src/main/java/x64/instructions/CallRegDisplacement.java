package x64.instructions;

import x64.operands.RegDisplacement;


public class CallRegDisplacement extends Instruction {

	/**
	 * Represents calling a method pointed to with the displacement of a register.
	 * @param registerDisplacement The displacement of a register, used as the pointer.
	 */
	public CallRegDisplacement(RegDisplacement registerDisplacement) {
		super("\tcall *" + registerDisplacement);
	}
}
