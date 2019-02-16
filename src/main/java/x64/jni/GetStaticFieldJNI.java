package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PreservedRegister;

public interface GetStaticFieldJNI extends CallJNIMethod {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param context The x64 function to add the instructions to
     * @param classReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param result The IL Register that is used for the type and the returned value.
     */
    default void addGetStaticField(X64Context context, X64PreservedRegister classReg,
                                   X64PreservedRegister fieldIDReg, Register result) {

        // load the args
        // arg1 = JNI
        context.loadJNI1();

        // arg2 = class reference
        context.addInstruction(
            new MoveInstruction(
                classReg,
                context.argumentRegister(2)
            )
        );

        // arg3 = field ID
        context.addInstruction(
            new MoveInstruction(
                fieldIDReg,
                context.argumentRegister(3)
            )
        );

        final JNIOffsets functionToCall = JNIOffsets.getStaticFieldOffset(result.getType());
        addCallJNI(context, functionToCall, result.toX64());
    }
}
