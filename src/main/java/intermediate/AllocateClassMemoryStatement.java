package intermediate;

import helper.CompileException;
import helper.Types;
import main.JavaCompiler;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.X64InstructionSize;
import x64.allocation.CallingConvention;
import x64.instructions.CallLabel;
import x64.instructions.MoveImmToReg;
import x64.jni.AllocObjectJNI;
import x64.jni.FindClassJNI;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToPseudo;
import x64.pseudo.MoveRegToPseudo;

import java.util.HashMap;

import static x64.allocation.CallingConvention.returnValueRegister;

public class AllocateClassMemoryStatement implements InterStatement, FindClassJNI, AllocObjectJNI {

	@NotNull private final Types type;
	@NotNull private final Register result;
	@NotNull private final String fileName;
	private final int line;

	/**
	 * Represents an allocation of memory for a class instance.
	 * @param type The fully-qualified class name.
	 * @param result The register to hold a reference to the memory allocated.
	 */
	public AllocateClassMemoryStatement(@NotNull Types type, @NotNull Register result,
										@NotNull String fileName, int line) {
		this.type = type;
		this.result = result;
		this.fileName = fileName;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return "allocateType " + result + " = " +  type + ";";
	}

	@Override
	public void typeCheck(@NotNull HashMap<Register, Types> regs, @NotNull HashMap<String, Types> locals,
						  @NotNull HashMap<String, Types> params, @NotNull InterFunction func) {
		
		result.setType(type);
		regs.put(result, type);
	}

	@Override
	public void compile(@NotNull X64Context context) throws CompileException {

		String className = type.getClassName(this.fileName, this.line);

		if (className.startsWith("java/")) {

			// use JNI
			// class = FindClass(JNIEnv*, className)
			X64PseudoRegister classReg = addFindClassJNICall(context, className);

			// object = AllocObject(JNIEnv*, class);
			X64PseudoRegister objectReg = addAllocObjectJNICall(context, classReg);

			// move object -> return value
			context.addInstruction(
				new MovePseudoToPseudo(
					objectReg,
					result.toX64()
				)
			);

		} else {

			// malloc (size_of class' instance structure) -> result
			InterFile temp = JavaCompiler.parseAndCompile(className, fileName, line);
			int size = temp.getClassSize();
			context.addInstruction(
				new MoveImmToReg(
					new Immediate(size),
					context.argumentRegister(1),
					X64InstructionSize.QUAD
				)
			);

			context.addInstruction(
				new CallLabel(CallingConvention.libraryFunc("malloc"))
			);

			// move returned value
			context.addInstruction(
				new MoveRegToPseudo(
					returnValueRegister(),
					result.toX64()
				)
			);

			// TODO result->function_table = Class_function_table used in virtual function calls
		}
	}
}
