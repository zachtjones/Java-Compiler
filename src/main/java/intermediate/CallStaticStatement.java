package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.jni.CallNonVirtualMethodJNI;
import x64.jni.FindClassJNI;
import x64.jni.GetMethodIdJNI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/** Represents a function call without a lookup. */
public class CallStaticStatement implements InterStatement, FindClassJNI, GetMethodIdJNI, CallNonVirtualMethodJNI {
	private final String className;
	private final String functionName;
	private final Register[] args;
	private final Register returnVal;

	private final String fileName;
	private final int line;


	public CallStaticStatement(String className, String functionName, Register[] args,
							   Register returnVal, String fileName, int line) {

		this.className = className;
		this.functionName = functionName;
		this.args = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "callStatic " + className + '.' + functionName + "("
				+ Arrays.toString(args).replaceAll("[]\\[]", "") + ") -> " + returnVal + ";";
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		
		if (returnVal != null) {
			// fill in the return type
			InterFile e = JavaCompiler.parseAndCompile(className, fileName, line);
			ArrayList<Types> argsList = new ArrayList<>();
			Arrays.stream(args).map(Register::getType).forEachOrdered(argsList::add);
			Types returnType = e.getReturnType(functionName, argsList);

			if (returnType == null) {
				final String signature = functionName + "(" +
					Arrays.stream(args)
					.map(i -> i.getType().getIntermediateRepresentation())
					.collect(Collectors.joining()) + ")";

				throw new CompileException("no method found with signature, " + signature
				+ ", referenced", fileName, line);
			}

			returnVal.setType(returnType);
			regs.put(returnVal, returnType);
		}
	}
}
