package x64;

public enum X64InstructionSize {
	// integer ones, quad can also be pointer
	BYTE('b'), WORD('w'), LONG('l'), QUAD('q'),

	// floating-point numbers
	SINGLE('s'), DOUBLE('d');

	public char size;
	X64InstructionSize(char suffix) {
		this.size = suffix;
	}

	@Override
	public String toString() {
		return Character.toString(size);
	}

	/** Returns if this is a floating point size. */
	public boolean isFloatingPoint() {
		return size == 's' || size == 'd';
	}
}
