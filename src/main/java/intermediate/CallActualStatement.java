package intermediate;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call without a lookup. */
public class CallActualStatement implements InterStatement, FindClassJNI, GetMethodIdJNI, CallNonVirtualMethodJNI {
	@NotNull private final Register obj;
	@NotNull private final String className;
	@NotNull final String name;
	@NotNull private final Register[] unconvertedArgs;
	@Nullable private final Register returnVal;
	
	@NotNull private final String fileName;
	private final int line;

	// filled in during type check
	private InterFile.MethodMatch match;
	private List<Register> convertedArgs;
	
	public CallActualStatement(@NotNull Register obj, @NotNull String className, @NotNull String name,
			@NotNull Register[] args, @Nullable Register returnVal, @NotNull String fileName, int line) {
		
		this.obj = obj;
		this.className = className;
		this.name = name;
		this.unconvertedArgs = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "call " + obj + " " + className + '.' + name + "(" 
				+ Arrays.toString(unconvertedArgs).replaceAll("[]\\[]", "") + ") -> " + returnVal + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {

		for (Register r : unconvertedArgs) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}

		// get information on converting the arguments
		InterFile e = JavaCompiler.parseAndCompile(obj.getType().getClassName(fileName, line), fileName, line);

		List<Register> ogArgs = Arrays.asList(unconvertedArgs);
		// one for each where the destination will be
		convertedArgs = ogArgs.stream()
			.map(i -> func.allocator.getNext(Types.UNKNOWN))
			.collect(Collectors.toList());

		match = e.getReturnType(name, ogArgs, convertedArgs, fileName, line);

		if (returnVal != null) {

			returnVal.setType(match.match.returnType);
			regs.put(returnVal, returnVal.getType());
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
				addGetMethodId(context, clazz, name, convertedArgs, returnVal);

			// add the conversion for the args
			for (List<InterStatement> statementList : match.conversionsToArgs) {
				// each arg
				for (InterStatement j : statementList) {
					j.compile(context);
				}
			}

			// result = CallNonVirtual<Type>Method(JNIEnv, obj, methodID, ...)
			addCallNonVirtualMethodJNI(context, clazz, objReg, methodId, convertedArgs, returnVal);

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

			// the rest of the args -- to the conversion
			for (int i = 0; i < unconvertedArgs.length; i++) {
				// compile in the conversion
				for (InterStatement j : match.conversionsToArgs.get(i)) {
					j.compile(context);
				}
				// move to the hardware arg
				context.addInstruction(
					new MovePseudoToReg(
						convertedArgs.get(i).toX64(),
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
