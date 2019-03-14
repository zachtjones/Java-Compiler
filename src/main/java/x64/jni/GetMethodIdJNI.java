package x64.jni;

import intermediate.Register;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import x64.X64Context;
import x64.jni.helpers.GetIdJNI;
import x64.operands.X64PseudoRegister;

import java.util.Arrays;
import java.util.stream.Collectors;

import static x64.jni.JNIOffsets.GET_METHOD_ID;

public interface GetMethodIdJNI extends GetIdJNI {

    /** Adds the code to get a method id. */
    default X64PseudoRegister addGetMethodId(@NotNull X64Context context, @NotNull X64PseudoRegister classReg,
                                             @NotNull String name, @NotNull Register[] args,
                                             @NotNull Register returnType) {

        final String argsSig = Arrays.stream(args)
            .map(r -> r.getType().getIntermediateRepresentation())
            .collect(Collectors.joining());

        // example: main method is (Ljava/lang/String;)V
        final String signature = "(" + argsSig + ")" + returnType.getType().getIntermediateRepresentation();

        return addGetIdJNICall(GET_METHOD_ID, name, signature, context, classReg);
    }
}
