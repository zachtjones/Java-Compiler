package x64.instructions;

import x64.operands.RegisterDisplacement;


public class CallRegDisplacement extends Instruction {
	private RegisterDisplacement registerDisplacement;

	public CallRegDisplacement(RegisterDisplacement registerDisplacement) {
		this.registerDisplacement = registerDisplacement;
	}

	@Override
	public String assemblyRepresentation() {
		return "\tcall *" + registerDisplacement;
	}
}
