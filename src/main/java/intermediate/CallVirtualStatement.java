package intermediate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import x64.X64Context;
import x64.instructions.CallClassMethod;
import x64.instructions.MoveInstruction;
import x64.jni.CallMethodJNI;
import x64.jni.GetMethodIdJNI;
import x64.jni.GetObjectClassJNI;
import x64.operands.X64RegisterOperand;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement, GetObjectClassJNI, GetMethodIdJNI, CallMethodJNI {
	private final Register obj;
	String name;
	private final Register[] args;
	private final Register returnVal;
	
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
		Arrays.toString(args).replaceAll("[\\[\\]]", "") + ")";
		if (returnVal != null) {
			return leftPart + " -> " + returnVal + ";";	
		} else {
			return leftPart + ";";
		}
	}

	@Override
	public void typeCheck(HashMap<Register, Types> regs, HashMap<String, Types> locals,
						  HashMap<String, Types> params, InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		
		if (returnVal != null) {
			// fill in the return type
			InterFile e = JavaCompiler.parseAndCompile(obj.getType().getClassName(fileName, line), fileName, line);

			ArrayList<Types> argsList = new ArrayList<>();
			Arrays.stream(args).map(Register::getType).forEachOrdered(argsList::add);
			Types returnType = e.getReturnType(name, argsList);
			
			if (returnType == null) {
				String signature = argsList.stream()
					.map(Types::getIntermediateRepresentation)
					.collect(Collectors.joining());
				throw new CompileException("no method in " + e.getName() + " found with signature, " + signature
						+ ", referenced", fileName, line);
			}
			
			returnVal.setType(returnType);
			regs.put(returnVal, returnType);
		}
	}

	@Override
	public void compile(X64Context context) throws CompileException {
		// if the type of the register is java/*, use JNI
		final String classname = obj.getType().getClassName(fileName, line);
		if (classname.startsWith("java/")) {

			final X64RegisterOperand objReg = obj.toX64();

			// clazz = GetClass
			final X64RegisterOperand clazz = addGetObjectClass(context, objReg);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			X64RegisterOperand methodId = addGetMethodId(context, clazz, name, args, returnVal);

			// result = Call<Type>Method(JNIEnv, obj, methodID, ...)
			addCallMethodJNI(context, objReg, methodId, args, returnVal);

		} else {
			// TODO requires adding the virtual function tables to the system of files

			// call class_method(JNI, object, ...args)
			context.loadJNI1();

			context.addInstruction(
				new MoveInstruction(obj.toX64(), argumentRegister(2))
			);

			// the rest of the args
			for (int i = 0; i < args.length; i++) {
				context.addInstruction(
					new MoveInstruction(args[i].toX64(), argumentRegister(3 + i))
				);
			}

			// call
			context.addInstruction(new CallClassMethod(classname, name));

			// move result -- unless null (meaning void method)
			if (returnVal != null)
				context.addInstruction(
					new MoveInstruction(returnValueRegister(), returnVal.toX64())
				);
		}
	}
}
