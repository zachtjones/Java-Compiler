package x64.directives;

/**
 * Represents a common symbol directive, which allocates a symbol to the linker of the size,
 * which is shared. Other files can use the same declaration, and the linker will resolve all the labels
 * to the same memory location.
 */
public class CommonSymbol implements Directive {
	private final String symbolName;
	private final int size;

	public CommonSymbol(String symbolName, int size) {
		this.symbolName = symbolName;
		this.size = size;
	}

	@Override
	public String toString() {
		return ".comm " + symbolName + ", " + size;
	}
}
