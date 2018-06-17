package intermediate;

import java.util.Arrays;

/** Represents a function call without a lookup. */
public class CallActualStatement implements InterStatement {
	Register obj;
	String className;
	String name;
	Register[] args;
	Register returnVal;
	
	public CallActualStatement(Register obj, String className, String name, Register[] args, Register returnVal) {
		this.obj = obj;
		this.className = className;
		this.name = name;
		this.args = args;
		this.returnVal = returnVal;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "call " + obj + " " + className + '.' + name + "(" 
				+ Arrays.toString(args).replaceAll("\\[|\\]", "") + ") -> " + returnVal + ";";
	}
}
