package x64.jni;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.pseudo.MovePseudoToReg;

public interface SetObjectArrayElementJNI extends CallJNIMethod {

	default void addSetObjectArrayElement(@NotNull X64Context context, @NotNull Register array,
										  @NotNull Register index, @NotNull Register value) {

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

		// arg4 = value
		context.addInstruction(
			new MovePseudoToReg(
				value.toX64(),
				context.argumentRegister(4)
			)
		);

		// call it -- method is void so no return value
		addCallVoidJNI(context, JNIOffsets.SET_OBJECT_ARRAY_ELEMENT);
	}
}
