package x64.jni.helpers;

import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.JNIOffsets;
import x64.operands.*;

import static x64.operands.X64RegisterOperand.of;

public interface CallJNIMethod {

    default void addCallJNI(X64Function function, JNIOffsets jniOffset, X64RegisterOperand resultHolder) {

        // mov %javaEnv*, %javaEnv
        final X64RegisterOperand temp2 = of(X64PreservedRegister.newTempQuad(function.getNextFreeRegister()));
        function.addInstruction(
            new MoveInstruction(
                new MemoryAtRegister(function.javaEnvPointer),
                temp2
            )
        );

        // mov JNI_METHOD_Offset(%javaEnv), %temp
        final X64RegisterOperand temp = of(X64PreservedRegister.newTempQuad(function.getNextFreeRegister()));
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(jniOffset.getOffset(), temp2),
                temp
            )
        );

        // call *%temp
        function.addInstruction(
            new CallFunctionPointerInstruction(
                temp
            )
        );

        // mov %RAX, %result holder
        function.addInstruction(
            new MoveInstruction(
                X64NativeRegister.RAX,
                resultHolder
            )
        );
    }
}