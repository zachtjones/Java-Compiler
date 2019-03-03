package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64Context;
import x64.instructions.CallLabel;
import x64.jni.CallStaticMethodJNI;
import x64.jni.FindClassJNI;
import x64.jni.GetStaticMethodIdJNI;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;
import x64.pseudo.MoveRegToPseudo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call without a lookup. */
public class CallStaticStatement implements InterStatement, FindClassJNI, GetStaticMethodIdJNI, CallStaticMethodJNI {
	@NotNull private final String className;
	@NotNull private final String functionName;
	@NotNull private final List<Register> unconvertedArgs;
	@Nullable private final Register returnVal;

	@NotNull private final String fileName;
	private final int line;

	// filled in during type check
	private MethodMatch match;
	private List<Register> convertedArgs;


	public CallStaticStatement(@NotNull String className, @NotNull String functionName, @NotNull Register[] args,
							   @Nullable Register returnVal, @NotNull String fileName, int line) {

		this.className = className;
		this.functionName = functionName;
		this.unconvertedArgs = Arrays.asList(args);
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']'
		return "callStatic " + className + '.' + functionName + "("
				+ unconvertedArgs.stream().map(Register::toString).collect(Collectors.joining(", "))
				+ ") -> " + returnVal + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) throws CompileException {
		
		for (Register r : unconvertedArgs) {
			UsageCheck.verifyDefined(r, regs, fileName, line);
		}

		// get information on converting the arguments
		InterFile e = JavaCompiler.parseAndCompile(className, fileName, line);

		// one for each where the destination will be
		convertedArgs = unconvertedArgs.stream()
			.map(i -> func.allocator.getNext(Types.UNKNOWN))
			.collect(Collectors.toList());

		match = e.getReturnType(functionName, unconvertedArgs, convertedArgs, fileName, line);

		if (returnVal != null) {
			// fill in return type
			returnVal.setType(match.match.returnType);
			regs.put(returnVal, returnVal.getType());
		}
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {
		// if the type of the register is java/*, use JNI
		if (className.startsWith("java/")) {

			// clazz = FindClass
			final X64PseudoRegister clazz = addFindClassJNICall(context, className);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			final X64PseudoRegister methodId =
				addGetStaticMethodId(context, clazz, functionName, convertedArgs, returnVal);

			// add the conversion for the args
			for (List<InterStatement> statementList : match.conversionsToArgs) {
				// each arg
				for (InterStatement j : statementList) {
					j.compile(context);
				}
			}

			// result = CallNonVirtual<Type>Method(JNIEnv, clazz, methodID, ...)
			addCallStaticMethodJNI(context, clazz, methodId, convertedArgs, returnVal);

		} else {

			// 1. Move the arguments in
			context.loadJNI1(); // JNIEnv

			// the rest of the args, the actual ones to the method
			for (int i = 0; i < unconvertedArgs.size(); i++) {
				// compile in the conversion
				for (InterStatement j : match.conversionsToArgs.get(i)) {
					j.compile(context);
				}

				// move to hardware regs
				context.addInstruction(
					new MovePseudoToReg(
						convertedArgs.get(i).toX64(),
						context.argumentRegister(2 + i)
					)
				);
			}

			// 2. call CLASS_NAME_METHOD_NAME
			context.addInstruction(
				new CallLabel(className, functionName)
			);

			// 3. mov %rax, result
			if (returnVal != null) {
				context.addInstruction(
					new MoveRegToPseudo(
						returnValueRegister(),
						returnVal.toX64()
					)
				);
			}
		}
	}
}
