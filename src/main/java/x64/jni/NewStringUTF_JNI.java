package x64.jni;

import intermediate.Register;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import static x64.jni.JNIOffsets.NEW_STRING_UTF;

public interface NewStringUTF_JNI {

	/**
	 * Inserts the call NewStringUTF(JNIEnv, char*) into the function, with result holding the end value
	 * @param function The x64 function to add the instructions to
	 * @param chars The register holding the utf-8 characters
	 * @param result The IL register that holds the result of this call
	 */
	default void addNewStringUTF_JNI(X64Function function, X64PreservedRegister chars, Register result) {

		// arg 1
		function.loadJNI1();

		// arg 2
		function.addInstruction(
			new MoveInstruction(
				chars,
				X64NativeRegister.RSI
			)
		);

		// mov NewStringUTF_offset(%javaEnvOne), %temp
		final X64PreservedRegister temp = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
		function.addInstruction(
			new MoveInstruction(
				new RegisterRelativePointer(NEW_STRING_UTF.getOffset(), function.javaEnvPointer),
				temp
			)
		);

		// call *%temp
		function.addInstruction(
			new CallFunctionPointerInstruction(
				temp
			)
		);

		// mov %rax, result
		function.addInstruction(
			new MoveInstruction(
				X64NativeRegister.RAX,
				X64PreservedRegister.fromILRegister(result)
			)
		);
	}
}
