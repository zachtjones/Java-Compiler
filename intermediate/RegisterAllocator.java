package intermediate;

public class RegisterAllocator {
	private Register curr = new Register(0, 0);
	
	/** Gets the next register (of the type specified)
	 * @param type One of the constants of the Register class. */
	public Register getNext(int type) {
		curr = new Register(curr.num + 1, type);
		return curr;
	}
	
	/** Makes the current register a new label one, and returns it's number. */
	public int getNextLabel() {
		curr = new Register(curr.num + 1, Register.LABEL);
		return curr.num;
	}
	
	/** Gets the last number generated. */
	public Register getLast() {
		return curr;
	}
}
