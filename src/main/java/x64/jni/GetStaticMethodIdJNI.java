package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.GetIdJNI;
import x64.operands.X64PseudoRegister;

import java.util.List;
import java.util.stream.Collectors;

import static x64.jni.JNIOffsets.GET_STATIC_METHOD_ID;

public interface GetStaticMethodIdJNI extends GetIdJNI {

	/** Adds the code to get a static method id to the file. */
    default X64PseudoRegister addGetStaticMethodId(X64Context context, X64PseudoRegister classReg,
												   String name, List<Register> args, Register returnType) {

        final String argsSig = args.stream()
            .map(r -> r.getType().getIntermediateRepresentation())
            .collect(Collectors.joining());

        // example: main method is (Ljava/lang/String;)V
        final String signature = "(" + argsSig + ")" + returnType.getType().getIntermediateRepresentation();

        return addGetIdJNICall(GET_STATIC_METHOD_ID, name, signature, context, classReg);
    }
}
