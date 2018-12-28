package x64.jni;

import helper.CompileException;
import intermediate.Register;
import x64.SymbolNames;
import x64.X64File;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import static x64.jni.JNIOffsets.getCallMethodOffset;

public interface CallMethodJNI {

    default void addCallMethodJNI(X64File assemblyFile, X64Function function, X64PreservedRegister objReg,
        X64PreservedRegister methodId, Register[] args, Register returnVal) throws CompileException {

        // 3 options for the method call:
        // %result = Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);
        // %result = Call<type>MethodA(JNIEnv *env, jobject obj, jmethodID methodID, const jvalue *args);
        // %result = Call<type>MethodV(JNIEnv *env, jobject obj, jmethodID methodID, va_list args);

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

        // the actual program arguments to the function
        // if there are <= 3 args, will use the registers, otherwise use the jvalue* args way
        X64NativeRegister[] lastThree = { X64NativeRegister.RCX, X64NativeRegister.R8, X64NativeRegister.R9 };
        if (args.length <= 3) {
            // insert up to the number of registers required to fill up the args
            for (int i = 0; i < args.length; i++) {
                function.addInstruction(
                    new MoveInstruction(
                        X64PreservedRegister.fromILRegister(args[i]),
                        lastThree[i]
                    )
                );
            }

            // load the function pointer
            // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

            // mov Call<Type>Method(%javaEnvOne), %temp
            final X64PreservedRegister temp = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
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
                    X64PreservedRegister.fromILRegister(returnVal)
                )
            );

        } else {
            // use the jvalue* args way

            // malloc the block
            // move the args over
            // call the function
            // free the block

            throw new CompileException("Call virtual with 3+ args not implemented yet.", assemblyFile.getFileName(), -1);
        }
    }
}
