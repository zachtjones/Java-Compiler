package tree;

/**
 * This class is used to hold a small history of the current compilation process.
 * For example, this stores if the last part compiled was "this", or "this.x",
 * so if the next Node is (a+b, c), the compiler knows what to do.
 */
public class CompileHistory {
	
	private boolean lastWasThis;

	private String twoNamesAgo;
	private String lastName; // name last used
	
	/** Call if the last expression parsed was "this" */
	public void setThis() {
		lastWasThis = true;
	}
	
	public boolean wasThisLast() {
		return lastWasThis;
	}

	/** Call if the last expression was a name. */
	public void setName(String name) {
		twoNamesAgo = lastName;
		lastName = name;
		lastWasThis = false;
	}
	
	/** Returns null, or the name last used. */
	public String getName() {
		return lastName;
	}

	/** Returns null, or the name used before the last used name. */
	public String getTwoNamesAgo() { return twoNamesAgo; }
}
