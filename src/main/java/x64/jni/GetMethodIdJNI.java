package x64.jni;

import intermediate.Register;
import x64.X64File;
import x64.X64Function;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.Arrays;
import java.util.stream.Collectors;

import static x64.SymbolNames.getJNISignatureFromILType;

public interface GetMethodIdJNI extends GetIdJNI {

    default X64RegisterOperand addGetMethodId(X64File assemblyFile, X64Function function,
                                                X64RegisterOperand classReg, String name, Register[] args,
                                                Register returnType) {

        final String argsSig = Arrays.stream(args)
            .map(r -> getJNISignatureFromILType(r.typeFull))
            .collect(Collectors.joining());

        // example: main method is (Ljava/lang/String;)V
        final String signature = "(" + argsSig + ")" + getJNISignatureFromILType(returnType.typeFull);

        final int jniOffset = JNIOffsets.GET_METHOD_ID.getOffset();

        return addGetIdJNICall(jniOffset, name, signature, assemblyFile, function, classReg);
    }
}
