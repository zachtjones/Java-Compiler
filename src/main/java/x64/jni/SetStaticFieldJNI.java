package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PreservedRegister;
import x64.pseudo.MovePseudoToReg;

public interface SetStaticFieldJNI extends CallJNIMethod {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param context The x64 function to add the instructions to
     * @param classReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param value The IL Register that holds the value to set
     */
    default void addSetStaticField(X64Context context, X64PreservedRegister classReg,
                                   X64PreservedRegister fieldIDReg, Register value) {

        // load the args
        // arg1 = JNI
        context.loadJNI1();

        // arg2 = class reference
        context.addInstruction(
            new MovePseudoToReg(
                classReg,
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

        final JNIOffsets functionToCall = JNIOffsets.setStaticFieldOffset(value.getType());

        addCallVoidJNI(context, functionToCall);
    }
}
