package x64.directives;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import java.util.Map;

/**
 * Represents a common symbol directive, which allocates a symbol to the linker of the size,
 * which is shared. Other files can use the same declaration, and the linker will resolve all the labels
 * to the same memory location.
 */
public class CommonSymbol implements Instruction {
	private final String symbolName;
	private final int size;

	public CommonSymbol(String symbolName, int size) {
		this.symbolName = symbolName;
		this.size = size;
	}

	@Override
	public boolean isCalling() {
		return false;
	}

	@Override
	public void markRegisters(int i, RegistersUsed usedRegs) {
		// no registers used
	}

	@Override
	public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
		// no registers used
	}

	@Override
	public String toString() {
		return ".comm " + symbolName + ", " + size;
	}
}
