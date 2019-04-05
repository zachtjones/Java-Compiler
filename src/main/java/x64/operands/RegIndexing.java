package x64.operands;

public class RegIndexing {
	public final X64Register base, index;
	public final int offset, scaling;

	RegIndexing(int offset, X64Register base, X64Register index, int scaling)  {
		this.offset = offset;
		this.scaling = scaling;
		this.base = base;
		this.index = index;
	}

	@Override
	public String toString() {
		return offset + "(" + base + ", " + index + ", " + scaling + ")";
	}
}
