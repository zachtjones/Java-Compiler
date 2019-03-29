package x64.jni;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.X64InstructionSize;
import x64.instructions.MoveImmToReg;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.Immediate;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

public interface NewObjectArrayJNI extends CallJNIMethod {

	/**
	 * add the code for the new primitive array JNI
	 * @param context The x64 context to add the instructions to
	 * @param clazz The JNI class that represents the elements' types.
	 * @param length The length of the array to create.
	 * @param returnVal Where to store the returned value
	 */
	default void addNewObjectArrayJNI(@NotNull X64Context context, @NotNull X64PseudoRegister clazz,
									  @NotNull Register length, @NotNull Register returnVal) {

		// for all practical purposes, we're going to have null as the initial element
		//  there could be optimizations to use another initial element.
		// %result = newObjectArray(JNI*, length, clazz, initialElement)

		// arg 1
		context.loadJNI1();

		// arg 2
		context.addInstruction(
			new MovePseudoToReg(
				length.toX64(),
				context.argumentRegister(2)
			)
		);

		// arg3, the class
		context.addInstruction(
			new MovePseudoToReg(
				clazz,
				context.argumentRegister(3)
			)
		);

		// arg4, null for the elements that it contains initially
		context.addInstruction(
			new MoveImmToReg(
				new Immediate(0),
				context.argumentRegister(4),
				X64InstructionSize.QUAD
			)
		);


		addCallJNI(context, JNIOffsets.NEW_OBJECT_ARRAY, returnVal.toX64());
	}

}
