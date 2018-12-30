package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64File;
import x64.X64Function;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

public interface GetStaticFieldIdJNI extends GetIdJNI {

    /**
     * Adds the code for returned = GetStaticFieldID(JNI, class, char* name, char* sig)
     * @param type The IL Register that holds the type of the field to be received
     * @param fieldName The name of the field to get
     * @param classReg The register that was received by FindClass(JNI, char* name)
     * @param assemblyFile The X64 assembly file to use for the data section
     * @param function The function to add the instructions to.
     * @return A new register that holds the ID that points to a class field field.
     */
    default X64RegisterOperand addGetStaticFieldIdJNICall(Register type, String fieldName,
            X64RegisterOperand classReg, X64File assemblyFile, X64Function function) {

        final String signature = SymbolNames.getJNISignatureFromILType(type.typeFull);
        final int jniOffset = JNIOffsets.getStaticFieldOffset(signature);

        return addGetIdJNICall(jniOffset, fieldName, signature, assemblyFile, function, classReg);
    }
}
