package intermediate;

import helper.CompileException;
import helper.Types;
import helper.UsageCheck;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64Context;
import x64.instructions.CallLabel;
import x64.jni.CallMethodJNI;
import x64.jni.GetMethodIdJNI;
import x64.jni.GetObjectClassJNI;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;
import x64.pseudo.MoveRegToPseudo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static x64.allocation.CallingConvention.returnValueRegister;

/** Represents a function call via v-table lookup. */
public class CallVirtualStatement implements InterStatement, GetObjectClassJNI, GetMethodIdJNI, CallMethodJNI {
	@NotNull private final Register obj;
	@NotNull private final String name;
	@NotNull private final Register[] unconvertedArgs;
	@Nullable private final Register returnVal;
	
	@NotNull private final String fileName;
	private final int line;

	// filled in during type check
	private MethodMatch match;
	private List<Register> convertedArgs;

	public CallVirtualStatement(@NotNull Register obj, @NotNull String name, @NotNull Register[] args,
								@Nullable Register returnVal, @NotNull String fileName, int line) {
		
		this.obj = obj;
		this.name = name;
		this.unconvertedArgs = args;
		this.returnVal = returnVal;
		this.fileName = fileName;
		this.line = line;
	}

	@Override
	public String toString() {
		// use the Arrays.toString and remove '[' and ']', also handle no return
		String leftPart = "callVirtual " + obj + " " + name + "(" + 
		Arrays.toString(unconvertedArgs).replaceAll("[\\[\\]]", "") + ")";
		if (returnVal != null) {
			return leftPart + " -> " + returnVal + ";";	
		} else {
			return leftPart + ";";
		}
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
		final String classname = obj.getType().getClassName(fileName, line);
		if (classname.startsWith("java/")) {

			final X64PseudoRegister objReg = obj.toX64();

			// clazz = GetClass
			final X64PseudoRegister clazz = addGetObjectClass(context, objReg);

			// methodID =  GetMethodID(JNIEnv *env, jclass clazz, char *name, char *sig);
			final X64PseudoRegister methodId = addGetMethodId(context, clazz, name, convertedArgs, returnVal);

			// add the conversion for the args
			for (List<InterStatement> statementList : match.conversionsToArgs) {
				// each arg
				for (InterStatement j : statementList) {
					j.compile(context);
				}
			}

			// result = Call<Type>Method(JNIEnv, obj, methodID, ...)
			addCallMethodJNI(context, objReg, methodId, convertedArgs, returnVal);

		} else {
			// TODO requires adding the virtual function tables to the system of files

			// call class_method(JNI, object, ...args)
			context.loadJNI1();

			context.addInstruction(
				new MovePseudoToReg(obj.toX64(), context.argumentRegister(2))
			);

			// the rest of the args -- to the conversion
			for (int i = 0; i < unconvertedArgs.length; i++) {
				// compile in the conversion
				for (InterStatement j : match.conversionsToArgs.get(i)) {
					j.compile(context);
				}
				// move to the hardware arg
				context.addInstruction(
					new MovePseudoToReg(convertedArgs.get(i).toX64(), context.argumentRegister(3 + i))
				);
			}

			// call
			context.addInstruction(new CallLabel(classname, name));

			// move result -- unless null (meaning void method)
			if (returnVal != null)
				context.addInstruction(
					new MoveRegToPseudo(returnValueRegister(), returnVal.toX64())
				);
		}
	}
}
