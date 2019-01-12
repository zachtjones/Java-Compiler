package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64Function;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.allocation.CallingConvention.argumentRegister;

public interface GetStaticFieldJNI extends CallJNIMethod {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param function The x64 function to add the instructions to
     * @param classReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param result The IL Register that is used for the type and the returned value.
     */
    default void addGetStaticField(X64Function function, X64RegisterOperand classReg,
                                   X64RegisterOperand fieldIDReg, Register result) {

        final String fieldType = SymbolNames.getJNISignatureFromILType(result.typeFull);

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

        final JNIOffsets functionToCall = JNIOffsets.getStaticFieldOffset(fieldType);
        final X64RegisterOperand returned = result.toX64();

        addCallJNI(function, functionToCall, returned);
    }
}
