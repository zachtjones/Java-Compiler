package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;
import main.JavaCompiler;

/** Represents a function call without a lookup. */
public class CallActualStatement implements InterStatement {
	Register obj;
	String className;
	String name;
	Register[] args;
	Register returnVal;
	
	private final String fileName;
	private final int line;
	
	public CallActualStatement(Register obj, String className, String name, Register[] args, 
			Register returnVal, String fileName, int line) {
		
		this.obj = obj;
		this.className = className;
		this.name = name;
		this.args = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
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
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		
		if (returnVal != null) {
			// fill in the return type
			InterFile e = JavaCompiler.parseAndCompile(obj.typeFull, fileName, line);
			String returnType = e.getReturnType(name, args);

			if (returnType == null) {
				StringBuilder signature = new StringBuilder(name + "(");
				for (int i = 0; i < args.length; i++) {
					signature.append(args[i].typeFull);
					if (i != args.length - 1) {
						signature.append(',');
					}
				}
				signature.append(")");
				throw new CompileException("no method found with signature, " + signature.toString()
				+ ", referenced", fileName, line);
			}

			returnVal.typeFull = returnType;
			regs.put(returnVal, returnVal.typeFull);
		}
	}
}
