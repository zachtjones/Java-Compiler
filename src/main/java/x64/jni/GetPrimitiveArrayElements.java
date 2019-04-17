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

public interface GetPrimitiveArrayElements extends CallJNIMethod {

	/**
	 * Adds the code for the JNI call to get all the elements of a primitive array into a buffer.
	 * Make sure to ReleasePrimitiveArrayElements to save the results back to the JVM and deallocate the buffer.
	 * @param context The x64 context to add the instructions to.
	 * @param array The array that is going to be accessed.
	 * @return The register that holds the buffer allocated by the JVM.
	 */
	default X64PseudoRegister addGetPrimitiveArrayElements(@NotNull X64Context context, @NotNull Register array,
														   @NotNull Types elementType) {
		JNIOffsets offset = JNIOffsets.getPrimitiveArrayElementsOffset(elementType);

		// arg 1: JNI*
		context.loadJNI1();

		// arg2: array
		context.addInstruction(
			new MovePseudoToReg(
				array.toX64(),
				context.argumentRegister(2)
			)
		);

		// arg3: boolean* isCopy -- pass NULL, so it doesn't care
		context.addInstruction(
			new MoveImmToReg(
				new Immediate(0), // null
				context.argumentRegister(3),
				X64InstructionSize.QUAD
			)
		);

		// call the offset
		X64PseudoRegister result = context.getNextQuadRegister(); // pointer size
		addCallJNI(context, offset, result);
		return result;
	}
}
