package x64.operands;

import org.jetbrains.annotations.NotNull;
import x64.X64InstructionSize;

/** This is an abstraction of a hardware register that is preserved across function calls */
public class X64PseudoRegister {

    private final int number;
    private final X64InstructionSize size;
    public X64PseudoRegister(int number, @NotNull X64InstructionSize size) {
        this.number = number;
        this.size = size;
    }

    public X64InstructionSize getSuffix() {
        return size;
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof X64PseudoRegister &&
            ((X64PseudoRegister) other).number == this.number;
    }

    @Override
    public String toString() {
        return "%" + size.size + number;
    }

    /** Returns if this represents a floating point register. */
	public boolean isFloatingPoint() {
		return size.isFloatingPoint();
	}

	/** Returns the number there is. */
    public int getNumber() {
        return this.number;
    }
}
