package x64.jni;

import x64.X64File;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.LoadEffectiveAddressInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.*;

import static x64.operands.X64RegisterOperand.of;


public interface GetIdJNI {

    /** All the GetFieldId, GetStaticFieldId, GetMethodId, GetStaticMethodId have a similar
     * method call, the difference being the class/object param and how to determine the char* sig parameter.
     * This interface is created as a helper for those specific ones
     * Returns the allocated register for the methodId/fieldId */
    default X64RegisterOperand addGetIdJNICall(int jniOffset, String fieldOrMethodName, String signature,
            X64File assemblyFile, X64Function function, X64RegisterOperand classReg) {

        // mov %javaEnvOne, %arg1
        function.loadJNI1();

        // mov %classReg, %arg2
        function.addInstruction(
            new MoveInstruction(
                classReg,
                X64NativeRegister.RSI
            )
        );

        // add field name to the data strings -> %arg3
        String fieldNameLabel = assemblyFile.insertDataString(fieldOrMethodName);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                PCRelativeData.pointerFromLabel(fieldNameLabel),
                X64NativeRegister.RDX
            )
        );

        // add the signature of the field type -> %arg4
        final String fieldTypeLabel = assemblyFile.insertDataString(signature);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                PCRelativeData.pointerFromLabel(fieldTypeLabel),
                X64NativeRegister.RCX
            )
        );

        // mov GetFieldID_Offset(%javaEnvOne), %temp
        final X64RegisterOperand temp = of(X64PreservedRegister.newTempQuad(function.getNextFreeRegister()));
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(jniOffset, function.javaEnvPointer),
                temp
            )
        );

        // call *%temp2
        function.addInstruction(
            new CallFunctionPointerInstruction(
                temp
            )
        );

        // mov %result, %class storage register
        final X64RegisterOperand fieldIDReg = of(X64PreservedRegister.newTempQuad(function.getNextFreeRegister()));
        function.addInstruction(
            new MoveInstruction(
                X64NativeRegister.RAX,
                fieldIDReg
            )
        );
        return fieldIDReg;
    }
}
