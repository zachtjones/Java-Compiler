package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.GetIdJNI;
import x64.operands.X64PreservedRegister;

import static x64.jni.JNIOffsets.GET_INSTANCE_FIELD_ID;

public interface GetInstanceFieldIdJNI extends GetIdJNI {

    /**
     * Adds the code for returned = GetInstanceFieldID(JNI, class, char* name, char* sig)
     * @param type The IL Register that holds the type of the field to be received
     * @param fieldName The name of the field to get
     * @param classReg The register that was received by FindClass(JNI, char* name)
     * @param context The X64 context to read and add instructions to
     * @return A new register that holds the ID that points to a class field field.
     */
    default X64PreservedRegister addGetInstanceFieldIdJNICall(Register type, String fieldName,
                                                              X64PreservedRegister classReg, X64Context context) {

        final String signature = type.getType().getIntermediateRepresentation();

        return addGetIdJNICall(GET_INSTANCE_FIELD_ID, fieldName, signature, context, classReg);
    }
}
