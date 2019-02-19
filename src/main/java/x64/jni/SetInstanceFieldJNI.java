package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

public interface SetInstanceFieldJNI extends CallJNIMethod {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param context The x64 function to add the instructions to
     * @param objReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param value The IL Register that holds the value to set
     */
    default void addSetInstanceField(X64Context context, Register objReg,
									 X64PseudoRegister fieldIDReg, Register value) {

        // load the args
        // arg1 = JNI
        context.loadJNI1();

        // arg2 = class reference
        context.addInstruction(
            new MovePseudoToReg(
                objReg.toX64(),
                context.argumentRegister(2)
            )
        );

        // arg3 = field ID
        context.addInstruction(
            new MovePseudoToReg(
                fieldIDReg,
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

        final JNIOffsets functionToCall = JNIOffsets.setInstanceFieldOffset(value.getType());

        addCallVoidJNI(context, functionToCall);
    }
}
