package intermediate;

import java.util.Arrays;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement {
	Register obj;
	String name;
	Register[] result;
	
	public CallVirtualStatement(Register obj, String name, Register[] result) {
		this.obj = obj;
		this.name = name;
		this.result = result;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "callVirtual " + obj + " " + name + "(" + Arrays.toString(result).replaceAll("\\[|\\]", "") + ");";
	}
}
