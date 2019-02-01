package intermediate;

import helper.Types;
import org.jetbrains.annotations.NotNull;

public class RegisterAllocator {
	@NotNull private Register curr = new Register(0, Types.UNKNOWN, "", -1);
	@NotNull private Register before = new Register(0, Types.UNKNOWN, "", -1);

	/**
	 * Gets the next register (of the type specified)
	 * @param type A Types instance that is the intermediate file's representation.
	 */
	public Register getNext(@NotNull Types type) {
		before = curr;
		// this one won't get Compile called on
		curr = new Register(curr.num + 1, type, "", -1);
		return curr;
	}
	
	/** Makes the current register a new label one, and returns it's number. */
	public int getNextLabel() {
		before = curr;
		// won't get filename and line called
		curr = new Register(curr.num + 1, Types.LABEL, "", -1);
		return curr.num;
	}
	
	/** Gets the last number generated. */
	public Register getLast() {
		return curr;
	}
	
	/** Gets the register before the last register generated. */
	public Register get2Before() {
		return before;
	}

}
