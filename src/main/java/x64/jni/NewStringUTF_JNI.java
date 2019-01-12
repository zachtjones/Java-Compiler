package x64.jni;

import intermediate.Register;
import x64.X64Function;
import x64.allocation.CallingConvention;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.NEW_STRING_UTF;

public interface NewStringUTF_JNI extends CallJNIMethod {

	/**
	 * Inserts the call NewStringUTF(JNIEnv, char*) into the function, with result holding the end value
	 * @param function The x64 function to add the instructions to
	 * @param chars The register holding the utf-8 characters
	 * @param result The IL register that holds the result of this call
	 */
	default void addNewStringUTF_JNI(X64Function function, X64RegisterOperand chars, Register result) {

		// arg 1
		function.loadJNI1();

		// arg 2
		function.addInstruction(
			new MoveInstruction(
				chars,
				CallingConvention.argumentRegister(2)
			)
		);

		final X64RegisterOperand returned = result.toX64();

		addCallJNI(function, NEW_STRING_UTF, returned);
	}
}
