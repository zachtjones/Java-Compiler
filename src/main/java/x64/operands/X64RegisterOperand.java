package x64.operands;

import x64.Instruction;
import x64.allocation.RegistersUsed;

public class X64RegisterOperand implements SourceOperand, DestinationOperand {


	private final X64NativeRegister nativeOne;
	private final X64PreservedRegister preservedOne;

	private X64RegisterOperand(X64NativeRegister nativeOne, X64PreservedRegister preservedOne) {
		this.nativeOne = nativeOne;
		this.preservedOne = preservedOne;
	}

	/** Creates a x64 Register Operand from the native register */
	public static X64RegisterOperand of(X64NativeRegister nativeOne) {
		return new X64RegisterOperand(nativeOne, null);
	}

	/** Creates a x64 register operand from the preserved pseudo register */
	public static X64RegisterOperand of(X64PreservedRegister preservedOne) {
		return new X64RegisterOperand(null, preservedOne);
	}

	@Override
	public Instruction.Size getSuffix() {
		return nativeOne != null ? nativeOne.getSuffix() : preservedOne.getSuffix();
	}

	@Override
	public void markDefined(int i, RegistersUsed usedRegs) {
		if (preservedOne != null) {
			usedRegs.markDefined(preservedOne, i);
		}
	}

	@Override
	public String assemblyRep() {
		return nativeOne != null ? nativeOne.assemblyRep() : preservedOne.assemblyRep();
	}

	@Override
	public void markUsed(int i, RegistersUsed usedRegs) {
		if (preservedOne != null) {
			usedRegs.markUsed(preservedOne, i);
		}
	}
}
