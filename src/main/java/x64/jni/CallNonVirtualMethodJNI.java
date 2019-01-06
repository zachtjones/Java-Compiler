package x64.jni;

import intermediate.Register;
import x64.SymbolNames;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.*;

import static x64.jni.JNIOffsets.getCallNonVirtualMethodOffset;

public interface CallNonVirtualMethodJNI {

    /**
     * add the code for the non virtual method call
     * @param function The x64 function to add the instructions to
     * @param classReg The register that holds the reference obtained via Find/Get Class
     * @param objReg The object to pass in as 'this'
     * @param methodId The method ID obtained previously
     * @param args The program arguments to the function
     * @param returnVal Where to store the returned value
     */
    default void addCallNonVirtualMethodJNI(X64Function function, X64RegisterOperand classReg,
            X64RegisterOperand objReg, X64RegisterOperand methodId, Register[] args, Register returnVal) {

        // 3 options for the method call, but will use the first one
        // %result = CallNonVirtual<type>Method(JNIEnv, obj, class, methodID, ...);

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
                classReg,
                X64NativeRegister.RDX
            )
        );

        // arg 4
        function.addInstruction(
            new MoveInstruction(
                methodId,
                X64NativeRegister.RCX
            )
        );

        // arg 5+, the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.length; i++) {
            function.addInstruction(
                new MoveInstruction(
                    args[i].toX64(),
                    X64NativeRegister.argNumbered(i + 5)
                )
            );
        }

        // load the function pointer
        // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);
        final X64RegisterOperand temp2 = function.getNextQuadRegister();
        function.addInstruction(
            new MoveInstruction(
                new MemoryAtRegister(function.javaEnvPointer),
                temp2
            )
        );

        // mov CallNonVirtual<Type>Method(%javaEnvOne), %temp
        final X64RegisterOperand temp = function.getNextQuadRegister();
        final String nativeType = SymbolNames.getJNISignatureFromILType(returnVal.typeFull);
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(getCallNonVirtualMethodOffset(nativeType), temp2),
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
                returnVal.toX64()
            )
        );
    }
}
