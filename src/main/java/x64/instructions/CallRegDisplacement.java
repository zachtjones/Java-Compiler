package x64.instructions;

import x64.operands.RegDisplacement;


public class CallRegDisplacement extends Instruction {
	private RegDisplacement registerDisplacement;

	public CallRegDisplacement(RegDisplacement registerDisplacement) {
		this.registerDisplacement = registerDisplacement;
	}

	@Override
	public String assemblyRepresentation() {
		return "\tcall *" + registerDisplacement;
	}
}
