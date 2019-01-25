package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.allocation.CallingConvention.argumentRegister;

public interface SetStaticFieldJNI extends CallJNIMethod {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param function The x64 function to add the instructions to
     * @param classReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param value The IL Register that holds the value to set
     */
    default void addSetStaticField(X64Context function, X64RegisterOperand classReg,
                                   X64RegisterOperand fieldIDReg, Register value) {

        // load the args
        // arg1 = JNI
        function.loadJNI1();

        // arg2 = class reference
        function.addInstruction(
            new MoveInstruction(
                classReg,
                argumentRegister(2)
            )
        );

        // arg3 = field ID
        function.addInstruction(
            new MoveInstruction(
                fieldIDReg,
                argumentRegister(3)
            )
        );

        // arg4 = value
        function.addInstruction(
            new MoveInstruction(
                value.toX64(),
                argumentRegister(4)
            )
        );

        final JNIOffsets functionToCall = JNIOffsets.setStaticFieldOffset(value.getType());

        addCallVoidJNI(function, functionToCall);
    }
}
