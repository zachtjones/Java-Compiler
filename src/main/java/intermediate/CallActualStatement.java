package intermediate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64Context;
import x64.instructions.CallLabel;
import x64.jni.CallNonVirtualMethodJNI;
import x64.jni.FindClassJNI;
import x64.jni.GetMethodIdJNI;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;
import x64.pseudo.MoveRegToPseudo;

import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call without a lookup. */
public class CallActualStatement implements InterStatement, FindClassJNI, GetMethodIdJNI, CallNonVirtualMethodJNI {
	@NotNull private final Register obj;
	@NotNull private final String className;
	@NotNull final String name;
	@NotNull private final Register[] args;
	@Nullable private final Register returnVal;
	
	@NotNull private final String fileName;
	private final int line;
	
	public CallActualStatement(@NotNull Register obj, @NotNull String className, @NotNull String name,
			@NotNull Register[] args, @Nullable Register returnVal, @NotNull String fileName, int line) {
		
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
				+ Arrays.toString(args).replaceAll("[]\\[]", "") + ") -> " + returnVal + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		for (Register r : args) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}
		
		if (returnVal != null) {
			// fill in the return type
			InterFile e = JavaCompiler.parseAndCompile(obj.getType().getClassName(fileName, line), fileName, line);
			ArrayList<Types> argsList = new ArrayList<>();
			Arrays.stream(args).map(Register::getType).forEachOrdered(argsList::add);
			Types returnType = e.getReturnType(name, argsList, fileName, line);

			returnVal.setType(returnType);
			regs.put(returnVal, returnType);
		}
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// if the type of the register is java/*, use JNI
		if (obj.getType().getClassName(fileName, line).startsWith("java/")) {

			final X64PseudoRegister objReg = obj.toX64();

			// clazz = FindClass
			final X64PseudoRegister clazz = addFindClassJNICall(context, className);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			final X64PseudoRegister methodId =
				addGetMethodId(context, clazz, name, args, returnVal);

			// result = CallNonVirtual<Type>Method(JNIEnv, obj, methodID, ...)
			addCallNonVirtualMethodJNI(context, clazz, objReg, methodId, args, returnVal);

		} else {

			// 1. Move the arguments in
			context.loadJNI1(); // JNIEnv

			// object
			context.addInstruction(
				new MovePseudoToReg(
					obj.toX64(),
					context.argumentRegister(2)
				)
			);

			// the rest of the args
			for (int i = 0; i < args.length; i++) {
				context.addInstruction(
					new MovePseudoToReg(
						args[i].toX64(),
						context.argumentRegister(3 + i)
					)
				);
			}

			// 2. call CLASS_NAME_METHOD_NAME
			context.addInstruction(
				new CallLabel(className, name)
			);

			// 3. mov %rax, result
			if (returnVal != null)
			context.addInstruction(
				new MoveRegToPseudo(
					returnValueRegister(),
					returnVal.toX64()
				)
			);
		}
	}
}
