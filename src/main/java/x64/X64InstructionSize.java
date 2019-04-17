package x64;

public enum X64InstructionSize {

	// integer ones, quad can also be pointer
	BYTE('b'), WORD('w'), LONG('l'), QUAD('q'),

	// floating-point numbers
	SINGLE('s'), DOUBLE('d');

	public static String concat(X64InstructionSize... sizes) {
		// we know the size of the resulting string is the number of elements.
		StringBuilder total = new StringBuilder(sizes.length);
		for (X64InstructionSize size : sizes)
			total.append(size.size);
		return total.toString();
	}

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
