package x64.jni;

import intermediate.Register;
import x64.X64Function;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.jni.JNIOffsets.getCallStaticMethodOffset;

public interface CallStaticMethodJNI extends CallJNIMethod {

    /**
     * add the code for the non virtual method call
     * @param function The x64 function to add the instructions to
     * @param classReg The register that holds the reference obtained via Find/Get Class
     * @param methodId The method ID obtained previously
     * @param args The program arguments to the function
     * @param returnVal Where to store the returned value
     */
    default void addCallStaticMethodJNI(X64Function function, X64RegisterOperand classReg, X64RegisterOperand methodId,
                                        Register[] args, Register returnVal) {

        // 3 options for the method call, but will use the first one
        // %result = CallNonVirtual<type>Method(JNIEnv, obj, class, methodID, ...);

        // arg 1
        function.loadJNI1();

        // arg 2
        function.addInstruction(
            new MoveInstruction(
                classReg,
                argumentRegister(2)
            )
        );

        // arg 3
        function.addInstruction(
            new MoveInstruction(
                methodId,
                argumentRegister(3)
            )
        );

        // arg 4+, the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.length; i++) {
            function.addInstruction(
                new MoveInstruction(
                    args[i].toX64(),
                    argumentRegister(i + 4)
                )
            );
        }

        addCallJNI(function, getCallStaticMethodOffset(returnVal.getType()), returnVal.toX64());
    }
}
