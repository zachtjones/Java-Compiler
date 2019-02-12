package x64.allocation;

/** This is a data class used for helping with the allocation of registers.
 * Instances of this class contain enough info to map to a hardware x64 register. */
public class RegisterMapped {
	final int num;
	final boolean needsPreserved;
	int priorityValue; // incremented every usage

	RegisterMapped(int num, boolean needsPreserved) {
		this.num = num;
		this.priorityValue = 1;
		this.needsPreserved = needsPreserved;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RegisterMapped &&
			((RegisterMapped) obj).needsPreserved == this.needsPreserved &&
			((RegisterMapped) obj).num == this.num;
	}

	@Override
	public String toString() {
		return "%" + (needsPreserved ? 'p' : 't') + num;
	}
}
