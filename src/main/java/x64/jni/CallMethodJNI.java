package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.getCallMethodOffset;
import static x64.operands.X64RegisterOperand.of;

public interface CallMethodJNI {

    default void addCallMethodJNI(X64Function function, X64RegisterOperand objReg,
            X64RegisterOperand methodId, Register[] args, Register returnVal) {

        // 3 options for the method call, using the first one:
        // %result = Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

        // arg 1
        function.loadJNI1();

        // arg 2
        function.addInstruction(
            new MoveInstruction(
                objReg,
                X64NativeRegister.RSI
            )
        );

        // arg 3
        function.addInstruction(
            new MoveInstruction(
                methodId,
                X64NativeRegister.RDX
            )
        );

        // arg 4+
        // the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.length; i++) {
            function.addInstruction(
                new MoveInstruction(
                    of(X64PreservedRegister.fromILRegister(args[i])),
                    X64NativeRegister.argNumbered(i + 4)
                )
            );
        }

        // load the function pointer
        // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

        // mov Call<Type>Method(%javaEnvOne), %temp
        final X64RegisterOperand temp = of(X64PreservedRegister.newTempQuad(function.getNextFreeRegister()));
        final String nativeType = SymbolNames.getJNISignatureFromILType(returnVal.typeFull);
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(getCallMethodOffset(nativeType), function.javaEnvPointer),
                temp
            )
        );

        // call *%temp
        function.addInstruction(
            new CallFunctionPointerInstruction(
                temp
            )
        );

        // mov %RAX, %result
        function.addInstruction(
            new MoveInstruction(
                X64NativeRegister.RAX,
                of(X64PreservedRegister.fromILRegister(returnVal))
            )
        );
    }
}
