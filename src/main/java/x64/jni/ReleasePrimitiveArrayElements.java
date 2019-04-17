package x64.jni;

import helper.Types;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.X64InstructionSize;
import x64.instructions.MoveImmToReg;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

public interface ReleasePrimitiveArrayElements extends CallJNIMethod {

	/**
	 * Adds the code for the JNI call to release all the elements of a buffer back into the primitive array.
	 * This code will copy the memory back if the JVM does a copy with getPrimitiveArrayElements.
	 * @param context The x64 context to add the instructions to.
	 * @param array The register that holds the array.
	 * @param buffer The buffer allocated by getPrimitiveArrayElements.
	 * @param primitiveType The type of the elements in the array.
	 */
	default void addReleasePrimitiveArrayElements(@NotNull X64Context context, @NotNull Register array,
															   @NotNull X64PseudoRegister buffer,
															   @NotNull Types primitiveType) {

		JNIOffsets offset = JNIOffsets.getReleasePrimitiveArrayElementsOffset(primitiveType);
		// void ReleasePrimitiveArrayElements(JNI*, array, buffer, mode);

		// arg 1: JNI*
		context.loadJNI1();

		// arg2: array
		context.addInstruction(
			new MovePseudoToReg(
				array.toX64(),
				context.argumentRegister(2)
			)
		);

		// arg3: buffer
		context.addInstruction(
			new MovePseudoToReg(
				buffer,
				context.argumentRegister(3)
			)
		);

		// arg4: int mode -- 0 is what I want: copy back and free buffer
		context.addInstruction(
			new MoveImmToReg(
				new Immediate(0), // null
				context.argumentRegister(4),
				X64InstructionSize.QUAD
			)
		);

		// call the offset
		addCallVoidJNI(context, offset);
	}
}
