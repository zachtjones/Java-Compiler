package x64.jni;

import intermediate.Register;
import x64.X64File;
import x64.X64Function;
import x64.jni.helpers.GetIdJNI;
import x64.operands.X64RegisterOperand;

import java.util.Arrays;
import java.util.stream.Collectors;

import static x64.jni.JNIOffsets.GET_STATIC_METHOD_ID;

public interface GetStaticMethodIdJNI extends GetIdJNI {

    default X64RegisterOperand addGetStaticMethodId(X64File assemblyFile, X64Function function,
											  X64RegisterOperand classReg, String name, Register[] args,
											  Register returnType) {

        final String argsSig = Arrays.stream(args)
            .map(r -> r.getType().getIntermediateRepresentation())
            .collect(Collectors.joining());

        // example: main method is (Ljava/lang/String;)V
        final String signature = "(" + argsSig + ")" + returnType.getType().getIntermediateRepresentation();

        return addGetIdJNICall(GET_STATIC_METHOD_ID, name, signature, assemblyFile, function, classReg);
    }
}