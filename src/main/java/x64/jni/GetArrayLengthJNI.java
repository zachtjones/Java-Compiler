package x64.jni;

import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

public interface GetArrayLengthJNI extends CallJNIMethod {

	/**
	 * Adds the code to load the length of an array.
	 * @param context The x64 context to add instructions to.
	 * @param array The register holding the array
	 * @param result The register that holds the result of getting the length.
	 */
	default void addGetArrayLength(@NotNull X64Context context, @NotNull X64PseudoRegister array,
								   @NotNull X64PseudoRegister result) {

		// int GetArrayLength(JNIEnv *env, array array);

		context.loadJNI1();

		context.addInstruction(
			new MovePseudoToReg(
				array,
				context.argumentRegister(2)
			)
		);

		// get the array length
		addCallJNI(context, JNIOffsets.GET_ARRAY_LENGTH, result);
	}
}
