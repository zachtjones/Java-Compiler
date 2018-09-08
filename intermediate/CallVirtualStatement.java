package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement {
	Register obj;
	String name;
	Register[] args;
	Register returnVal;
	
	private final String fileName;
	private final int line;
	
	public CallVirtualStatement(Register obj, String name, Register[] args, Register returnVal,
			String fileName, int line) {
		
		this.obj = obj;
		this.name = name;
		this.args = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "callVirtual " + obj + " " + name + "(" + Arrays.toString(args).replaceAll("\\[|\\]", "") + ") -> " + returnVal + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		System.out.println("check for definition of method: " + name + " in class: " + obj.typeFull);
		
		// TODO -- use the file system, probably create helper class
		returnVal.typeFull = "unknown"; // TODO
		regs.put(returnVal, returnVal.typeFull);
	}
}
