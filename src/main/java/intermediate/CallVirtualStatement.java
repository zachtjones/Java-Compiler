package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;
import main.JavaCompiler;

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
		// use the Arrays.toString and remove '[' and ']', also handle no return
		String leftPart = "callVirtual " + obj + " " + name + "(" + 
		Arrays.toString(args).replaceAll("\\[|\\]", "") + ")";
		if (returnVal != null) {
			return leftPart + " -> " + returnVal + ";";	
		} else {
			return leftPart + ";";
		}
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
