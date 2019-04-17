package x64.operands;

public class PseudoIndexing {
	public final X64PseudoRegister base, index;
	public final int offset, scaling;

	public PseudoIndexing(X64PseudoRegister base, X64PseudoRegister index, int scaling)  {
		this.offset = 0;
		this.scaling = scaling;
		this.base = base;
		this.index = index;
	}

	/***
	 * Converts this to an indexing with operand with the base and index swapped out.
	 * @param base The register the base gets allocated to.
	 * @param index The register the index gets allocated to.
	 * @return The register indexing operand allocated.
	 */
	public RegIndexing allocate(X64Register base, X64Register index) {
		return new RegIndexing(offset, base, index, scaling);
	}

	@Override
	public String toString() {
		return offset + "(" + base + ", " + index + ", " + scaling + ")";
	}
}
