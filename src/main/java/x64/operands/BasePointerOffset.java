package x64.operands;

/**
 * Represents a negative integer displacement from the register base pointer.
 */
public class BasePointerOffset {

	private final int offset;

	/** The offset in bytes, this should be a multiple of 8 and negative */
	public BasePointerOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return offset + "(%rbp)";
	}
}
