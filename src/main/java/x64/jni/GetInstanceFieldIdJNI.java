package x64.jni;

import intermediate.Register;
import x64.X64File;
import x64.X64Function;
import x64.jni.helpers.GetIdJNI;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.GET_INSTANCE_FIELD_ID;
import static x64.jni.JNIOffsets.GET_STATIC_FIELD_ID;

public interface GetInstanceFieldIdJNI extends GetIdJNI {

    /**
     * Adds the code for returned = GetInstanceFieldID(JNI, class, char* name, char* sig)
     * @param type The IL Register that holds the type of the field to be received
     * @param fieldName The name of the field to get
     * @param classReg The register that was received by FindClass(JNI, char* name)
     * @param assemblyFile The X64 assembly file to use for the data section
     * @param function The function to add the instructions to.
     * @return A new register that holds the ID that points to a class field field.
     */
    default X64RegisterOperand addGetInstanceFieldIdJNICall(Register type, String fieldName,
														  X64RegisterOperand classReg, X64File assemblyFile, X64Function function) {

        final String signature = type.getType().getIntermediateRepresentation();

        return addGetIdJNICall(GET_INSTANCE_FIELD_ID, fieldName, signature, assemblyFile, function, classReg);
    }
}
