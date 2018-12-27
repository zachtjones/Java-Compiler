package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

public interface GetStaticFieldJNI {

    /**
     * Adds the code result = GetStatic&lt;Type&gt;Field(JNI, class, fieldId)
     * @param function The x64 function to add the instructions to
     * @param classReg The x64 register holding the result of FindClass
     * @param fieldIDReg The x64 register holding the result of GetStaticFieldId
     * @param result The IL Register that is used for the type and the returned value.
     */
    default void addGetStaticField(X64Function function, X64PreservedRegister classReg,
                                   X64PreservedRegister fieldIDReg, Register result) {

        final String fieldType = SymbolNames.getJNISignatureFromILType(result.typeFull);

        // load the args
        function.loadJNI1();
        function.addInstruction(
            new MoveInstruction(
                classReg,
                X64NativeRegister.RSI
            )
        );
        function.addInstruction(
            new MoveInstruction(
                fieldIDReg,
                X64NativeRegister.RDX
            )
        );

        // mov GetFieldID_Offset(%javaEnvOne), %temp3
        final X64PreservedRegister temp3 = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(JNIOffsets.getStaticFieldOffset(fieldType), function.javaEnvPointer),
                temp3
            )
        );

        // call *%temp3
        function.addInstruction(
            new CallFunctionPointerInstruction(
                temp3
            )
        );

        // mov %result, %final result
        function.addInstruction(
            new MoveInstruction(
                X64NativeRegister.RAX,
                X64PreservedRegister.fromILRegister(result)
            )
        );
    }
}
