package x64.jni.helpers;

import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.JNIOffsets;
import x64.operands.*;

import static x64.allocation.CallingConvention.returnValueRegister;

public interface CallJNIMethod {

    default void addCallVoidJNI(X64Function function, JNIOffsets jniOffset) {

        // mov %javaEnv*, %javaEnv
        final X64RegisterOperand temp = function.getNextQuadRegister();
        function.addInstruction(
            new MoveInstruction(
                new MemoryAtRegister(function.javaEnvPointer),
                temp
            )
        );

        // call *JNI_METHOD_OFFSET(%javaEnv)
        function.addInstruction(
            new CallFunctionPointerInstruction(
                new RegisterRelativePointer(jniOffset.getOffset(), temp)
            )
        );
    }

    default void addCallJNI(X64Function function, JNIOffsets jniOffset, X64RegisterOperand resultHolder) {

        // treat like calling void method, then move result to the register
        addCallVoidJNI(function, jniOffset);

        // mov %RAX, %result holder
        function.addInstruction(
            new MoveInstruction(
                returnValueRegister(),
                resultHolder
            )
        );
    }
}
