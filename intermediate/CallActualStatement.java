package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;

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

	@Override
	public void typeCheck(HashMap<Register, String> regs, HashMap<String, String> locals,
			HashMap<String, String> params, InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs);
		}
		System.out.println("check for definition of method: " + name + " in class: " + className);
		
		// TODO -- use the file system, probably create helper class
		returnVal.typeFull = "unknown"; // TODO
		regs.put(returnVal, returnVal.typeFull);
	}
}
