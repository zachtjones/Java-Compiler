package x64.jni;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.pseudo.MovePseudoToReg;

public interface GetObjectArrayElementJNI extends CallJNIMethod {

	/**
	 * Adds the code to call a JNI function to get the result = GetObjectArrayElement(array, index)
	 * @param context The context to add the instruction to
	 * @param array The array to get the value at index of.
	 * @param index The index in the array.
	 * @param result The pointer to the object that should be filled in.
	 */
	default void addGetObjectArrayElement(@NotNull X64Context context, @NotNull Register array,
										  @NotNull Register index, @NotNull Register result) {

		// arg1 = JNI*
		context.loadJNI1();

		// arg2 = array
		context.addInstruction(
			new MovePseudoToReg(
				array.toX64(),
				context.argumentRegister(2)
			)
		);

		// arg3 = index
		context.addInstruction(
			new MovePseudoToReg(
				index.toX64(),
				context.argumentRegister(3)
			)
		);

		// call it -- method is void so no return value
		addCallJNI(context, JNIOffsets.GET_OBJECT_ARRAY_ELEMENT, result.toX64());

	}
}
