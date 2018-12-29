package x64.allocation;

import x64.Instruction;

import java.util.ArrayList;

public class RegisterTransformer {

	public AllocatedUnit allocate(ArrayList<Instruction> contents) {

		// determine the usages of the registers
		RegistersUsed usedRegs = new RegistersUsed();
		for (int i = 0; i < contents.size(); i++) {
			contents.get(i).markRegisters(i, usedRegs);
		}

		// now we can determine which ones need preserved across function calls
		// when splitting, include the call instruction in the previous list


		// determine the registers that actually need preserved = call happens between set/use

	}

}
