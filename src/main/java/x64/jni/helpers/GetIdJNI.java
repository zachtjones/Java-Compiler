package x64.jni.helpers;

import x64.X64Context;
import x64.instructions.LoadEffectiveAddressRIPRelativeToReg;
import x64.jni.JNIOffsets;
import x64.operands.RIPRelativeData;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

import static x64.X64InstructionSize.QUAD;


public interface GetIdJNI extends CallJNIMethod {

    /** All the GetFieldId, GetStaticFieldId, GetMethodId, GetStaticMethodId have a similar
     * method call, the difference being the class/object param and how to determine the char* sig parameter.
     * This interface is created as a helper for those specific ones
     * Returns the allocated register for the methodId/fieldId */
    default X64PseudoRegister addGetIdJNICall(JNIOffsets jniOffset, String fieldOrMethodName, String signature,
											  X64Context context, X64PseudoRegister classReg) {

        // mov %javaEnvOne, %arg1
        context.loadJNI1();

        // mov %classReg, %arg2
        context.addInstruction(
            new MovePseudoToReg(
                classReg,
                context.argumentRegister(2)
            )
        );

        // add field name to the data strings -> %arg3
        String fieldNameLabel = context.insertDataString(fieldOrMethodName);
        context.addInstruction(
            new LoadEffectiveAddressRIPRelativeToReg(
                RIPRelativeData.pointerFromLabel(fieldNameLabel),
                context.argumentRegister(3),
                QUAD
            )
        );

        // add the signature of the field type -> %arg4
        final String fieldTypeLabel = context.insertDataString(signature);
        context.addInstruction(
            new LoadEffectiveAddressRIPRelativeToReg(
                RIPRelativeData.pointerFromLabel(fieldTypeLabel),
                context.argumentRegister(4),
                QUAD
            )
        );

        // call the method, storing result in fieldIDReg
        final X64PseudoRegister fieldIDReg = context.getNextQuadRegister();
        addCallJNI(context, jniOffset, fieldIDReg);

        return fieldIDReg;
    }
}
