package intermediate;

import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64File;
import x64.X64Function;
import x64.instructions.CallClassMethod;
import x64.instructions.MoveInstruction;
import x64.jni.CallNonVirtualMethodJNI;
import x64.jni.FindClassJNI;
import x64.jni.GetMethodIdJNI;
import x64.operands.X64NativeRegister;
import x64.operands.X64RegisterOperand;

/** Represents a function call without a lookup. */
public class CallActualStatement implements InterStatement, FindClassJNI, GetMethodIdJNI, CallNonVirtualMethodJNI {
	private final Register obj;
	private final String className;
	String name;
	private final Register[] args;
	private final Register returnVal;
	
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

	@Override
	public void compile(X64File assemblyFile, X64Function function) throws CompileException {
		// if the type of the register is java/*, use JNI
		if (obj.typeFull.startsWith("java/")) {

			final X64RegisterOperand objReg = obj.toX64();

			// clazz = FindClass
			final X64RegisterOperand clazz = addFindClassJNICall(assemblyFile, function, className);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			final X64RegisterOperand methodId =
				addGetMethodId(assemblyFile, function, clazz, name, args, returnVal);

			// result = CallNonVirtual<Type>Method(JNIEnv, obj, methodID, ...)
			addCallNonVirtualMethodJNI(function, clazz, objReg, methodId, args, returnVal);

		} else {

			// 1. Move the arguments in
			function.loadJNI1(); // JNIEnv

			// object
			function.addInstruction(
				new MoveInstruction(
					obj.toX64(),
					X64NativeRegister.RSI
				)
			);

			// the rest of the args
			for (int i = 0; i < args.length; i++) {
				function.addInstruction(
					new MoveInstruction(
						args[i].toX64(),
						X64NativeRegister.argNumbered(3 + i)
					)
				);
			}

			// 2. call CLASS_NAME_METHOD_NAME
			function.addInstruction(
				new CallClassMethod(className, name)
			);

			// 3. mov %rax, result
			function.addInstruction(
				new MoveInstruction(
					X64NativeRegister.RAX,
					returnVal.toX64()
				)
			);
		}
	}
}
