package x64.allocation;

import x64.Instruction;
import x64.operands.X64NativeRegister;

import java.util.ArrayList;

public class AllocatedUnit {

	AllocatedUnit() {

	}

	public ArrayList<Instruction> afterAllocationInstructions = new ArrayList<>();
	public ArrayList<X64NativeRegister> preservedUsed = new ArrayList<>();
}
