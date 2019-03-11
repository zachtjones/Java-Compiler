package tree;

import helper.CompileException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to hold a small history of the current compilation process.
 * For example, this stores if the last part compiled was "this", or "this.x",
 * so if the next Node is (a+b, c), the compiler knows what to do.
 */
public class CompileHistory {
	
	private boolean lastWasThis;

	@Nullable private String twoNamesAgo;
	@Nullable private String lastName; // name last used


	@Nullable private String className;
	
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
	@NotNull public String getName() throws CompileException {
		if (lastName == null) throw new CompileException("the last name node was null", "", -1);
		return lastName;
	}

	/** Returns null, or the name used before the last used name. */
	@Nullable
	public String getTwoNamesAgo() throws CompileException {
		return twoNamesAgo;
	}

	public void clearClassName() {
		className = null;
	}

	public void setClassName(@NotNull String className) {
		this.className = className;
	}

	@NotNull
	public String getClassName() {
		if (className == null) throw new RuntimeException("use hasClassName before accessing getClassName");
		return className;
	}

	public boolean hasClassName() {
		return className != null;
	}
}
