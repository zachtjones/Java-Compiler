package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64Context;
import x64.instructions.CallClassMethod;
import x64.instructions.MoveInstruction;
import x64.jni.CallStaticMethodJNI;
import x64.jni.FindClassJNI;
import x64.jni.GetStaticMethodIdJNI;
import x64.operands.X64RegisterOperand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call without a lookup. */
public class CallStaticStatement implements InterStatement, FindClassJNI, GetStaticMethodIdJNI, CallStaticMethodJNI {
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

	@Override
	public void compile(X64Context context) throws CompileException {
		// if the type of the register is java/*, use JNI
		if (className.startsWith("java/")) {

			// clazz = FindClass
			final X64RegisterOperand clazz = addFindClassJNICall(context, className);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			final X64RegisterOperand methodId =
				addGetStaticMethodId(context, clazz, functionName, args, returnVal);

			// result = CallNonVirtual<Type>Method(JNIEnv, clazz, methodID, ...)
			addCallStaticMethodJNI(context, clazz, methodId, args, returnVal);

		} else {

			// 1. Move the arguments in
			context.loadJNI1(); // JNIEnv

			// the rest of the args, the actual ones to the method
			for (int i = 0; i < args.length; i++) {
				context.addInstruction(
					new MoveInstruction(
						args[i].toX64(),
						argumentRegister(2 + i)
					)
				);
			}

			// 2. call CLASS_NAME_METHOD_NAME
			context.addInstruction(
				new CallClassMethod(className, functionName)
			);

			// 3. mov %rax, result
			context.addInstruction(
				new MoveInstruction(
					returnValueRegister(),
					returnVal.toX64()
				)
			);
		}
	}
}
