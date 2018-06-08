package intermediate;

import java.util.Arrays;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement {
	Register obj;
	String name;
	Register[] args;
	
	public CallVirtualStatement(Register obj, String name, Register[] args) {
		this.obj = obj;
		this.name = name;
		this.args = args;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "callVirtual " + obj + " " + name + "(" + Arrays.toString(args).replaceAll("\\[|\\]", "") + ");";
	}
}
