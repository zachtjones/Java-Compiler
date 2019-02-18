package x64.jni.helpers;

import x64.X64Context;
import x64.pseudo.CallPseudoDisplacement;
import x64.jni.JNIOffsets;
import x64.operands.*;
import x64.pseudo.MovePseudoAbsoluteToPseudo;
import x64.pseudo.MoveRegToPseudo;

import static x64.allocation.CallingConvention.returnValueRegister;

public interface CallJNIMethod {

    /** Inserts the code to load the JNI pointer -> JNI structure -> virtual function table,
     * and calls the code. */
    default void addCallVoidJNI(X64Context context, JNIOffsets jniOffset) {

        // mov %javaEnv*, %javaEnv
        final X64PseudoRegister temp = context.getNextQuadRegister();
        context.addInstruction(
            new MovePseudoAbsoluteToPseudo(
                new PseudoAbsolute(context.getJniPointer()),
                temp
            )
        );

        // call *JNI_METHOD_OFFSET(%javaEnv)
        context.addInstruction(
            new CallPseudoDisplacement(
                new PseudoDisplacement(jniOffset.getOffset(), temp)
            )
        );
    }

    /** Inserts the code to load the JNI pointer -> JNI structure -> virtual function table,
     * calls the code, and moves the result to the resultHolder register. */
    default void addCallJNI(X64Context context, JNIOffsets jniOffset, X64PseudoRegister resultHolder) {

        // treat like calling void method, then move result to the register
        addCallVoidJNI(context, jniOffset);

        // mov %RAX, %result holder
        context.addInstruction(
            new MoveRegToPseudo(
                returnValueRegister(),
                resultHolder
            )
        );
    }
}
