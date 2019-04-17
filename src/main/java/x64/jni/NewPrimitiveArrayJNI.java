package x64.jni;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.pseudo.MovePseudoToReg;

public interface NewPrimitiveArrayJNI extends CallJNIMethod {

	/**
	 * add the code for the new primitive array JNI
	 * @param context The x64 context to add the instructions to
	 * @param type The JNI offset corresponding to the primitive type that an array of will be created.
	 * @param length The length of the array to create.
	 * @param returnVal Where to store the returned value
	 */
	default void addNewPrimitiveArrayJNI(@NotNull X64Context context, @NotNull JNIOffsets type,
											@NotNull Register length, @NotNull Register returnVal) {

		// %result = new<PrimitiveType>Array(JNI*, length)

		// arg 1
		context.loadJNI1();

		// arg 2
		context.addInstruction(
			new MovePseudoToReg(
				length.toX64(),
				context.argumentRegister(2)
			)
		);

		addCallJNI(context, type, returnVal.toX64());
	}

}
