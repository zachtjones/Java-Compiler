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
	
	public CallVirtualStatement(Register obj, String name, Register[] args, Register returnVal) {
		this.obj = obj;
		this.name = name;
		this.args = args;
		this.returnVal = returnVal;
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
			UsageCheck.verifyDefined(r, regs);
		}
		System.out.println("check for definition of method: " + name + " in class: " + obj.typeFull);
		
		// TODO -- use the file system, probably create helper class
		returnVal.typeFull = "unknown"; // TODO
		regs.put(returnVal, returnVal.typeFull);
	}
}
