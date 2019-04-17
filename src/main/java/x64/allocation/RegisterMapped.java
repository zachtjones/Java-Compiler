package x64.allocation;

import org.jetbrains.annotations.NotNull;

/** This is a data class used for helping with the allocation of registers.
 * Instances of this class contain enough info to map to a hardware x64 register. */
public class RegisterMapped implements Comparable<RegisterMapped> {
	final int num;
	final boolean needsPreserved;
	private int priorityValue; // incremented every usage

	RegisterMapped(int num, boolean needsPreserved) {
		this.num = num;
		this.priorityValue = 1;
		this.needsPreserved = needsPreserved;
	}

	public void add(int value) {
		priorityValue += value;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RegisterMapped &&
			((RegisterMapped) obj).needsPreserved == this.needsPreserved &&
			((RegisterMapped) obj).num == this.num;
	}

	@Override
	public int compareTo(@NotNull RegisterMapped o) {
		// must be consistent with equals here

		// highest priority first, opposite order as normal
		int primaryOrder = Integer.compare(o.priorityValue, this.priorityValue);
		if (primaryOrder != 0) {
			return primaryOrder;
		}

		// prioritize preserved ones to break tie with priority
		if (this.needsPreserved && !o.needsPreserved) {
			return -1;
		}
		if (!this.needsPreserved && o.needsPreserved) {
			return 1;
		}

		// using the number finally as an arbitrary tie-breaker. This isn't needed, but will help
		//  with the consistency of the ordering of items in the map
		// lowest number is first

		// if the priority, needsPreserved, and num are all the same, they are equal
		return Integer.compare(this.num, o.num);
	}

	@Override
	public String toString() {
		String value = "%" + (needsPreserved ? 'p' : 't') + num;
		if (priorityValue != 0) {
			value += "(p" + priorityValue + ")";
		}
		return value;
	}
}
