package x64.jni.helpers;

import x64.X64File;
import x64.X64Function;
import x64.instructions.LoadEffectiveAddressInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.JNIOffsets;
import x64.operands.*;

import static x64.allocation.CallingConvention.argumentRegister;


public interface GetIdJNI extends CallJNIMethod {

    /** All the GetFieldId, GetStaticFieldId, GetMethodId, GetStaticMethodId have a similar
     * method call, the difference being the class/object param and how to determine the char* sig parameter.
     * This interface is created as a helper for those specific ones
     * Returns the allocated register for the methodId/fieldId */
    default X64RegisterOperand addGetIdJNICall(JNIOffsets jniOffset, String fieldOrMethodName, String signature,
            X64File assemblyFile, X64Function function, X64RegisterOperand classReg) {

        // mov %javaEnvOne, %arg1
        function.loadJNI1();

        // mov %classReg, %arg2
        function.addInstruction(
            new MoveInstruction(
                classReg,
                argumentRegister(2)
            )
        );

        // add field name to the data strings -> %arg3
        String fieldNameLabel = assemblyFile.insertDataString(fieldOrMethodName);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                PCRelativeData.pointerFromLabel(fieldNameLabel),
                argumentRegister(3)
            )
        );

        // add the signature of the field type -> %arg4
        final String fieldTypeLabel = assemblyFile.insertDataString(signature);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                PCRelativeData.pointerFromLabel(fieldTypeLabel),
                argumentRegister(4)
            )
        );

        // call the method, storing result in fieldIDReg
        final X64RegisterOperand fieldIDReg = function.getNextQuadRegister();
        addCallJNI(function, jniOffset, fieldIDReg);

        return fieldIDReg;
    }
}
