package intermediate;

public class RegisterAllocator {
	private Register curr = new Register(0, 0, "", -1);
	private Register before = new Register(0, 0, "", -1);
	
	/** Gets the next register (of the type specified)
	 * @param type One of the constants of the Register class. */
	public Register getNext(int type) {
		before = curr;
		curr = new Register(curr.num + 1, type, "", -1); // this one won't get Compile called on
		return curr;
	}
	
	/**
	 * Gets the next register (of the type specified)
	 * @param type A String that is the intermediate file's representation.
	 */
	public Register getNext(String type) {
		switch(type) {
		case "bool": return getNext(Register.BOOLEAN);
		case "char": return getNext(Register.CHAR);
		case "byte": return getNext(Register.BYTE);
		case "short": return getNext(Register.SHORT);
		case "int": return getNext(Register.INT);
		case "long": return getNext(Register.LONG);
		case "float": return getNext(Register.FLOAT);
		case "double": return getNext(Register.DOUBLE);
		}
		return getNext(Register.REFERENCE);
	}
	
	/** Makes the current register a new label one, and returns it's number. */
	public int getNextLabel() {
		curr = new Register(curr.num + 1, Register.LABEL, "", -1); // won't get filename and line called
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
