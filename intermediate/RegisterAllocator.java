package intermediate;

public class RegisterAllocator {
	private int num = 0;
	
	/**
	 * Gets the next number.
	 */
	public int getNum() {
		num++;
		return num;
	}
	
	/** Gets the last number generated. */
	public int getLast() {
		return num;
	}
}
