package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.getCallMethodOffset;
import static x64.operands.X64RegisterOperand.of;

public interface CallMethodJNI extends CallJNIMethod {

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

        // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...) -> returnVal
        // determine which type of object it is:
        final String nativeType = SymbolNames.getJNISignatureFromILType(returnVal.typeFull);
        final JNIOffsets offset = getCallMethodOffset(nativeType);

        final X64RegisterOperand result = of(X64PreservedRegister.fromILRegister(returnVal));

        addCallJNI(function, offset, result);
    }
}
