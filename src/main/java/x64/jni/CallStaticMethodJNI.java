package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PreservedRegister;
import x64.pseudo.MovePseudoRegToReg;

import static x64.jni.JNIOffsets.getCallStaticMethodOffset;

public interface CallStaticMethodJNI extends CallJNIMethod {

    /**
     * add the code for the non virtual method call
     * @param context The x64 context to add the code to
     * @param classReg The register that holds the reference obtained via Find/Get Class
     * @param methodId The method ID obtained previously
     * @param args The program arguments to the function
     * @param returnVal Where to store the returned value
     */
    default void addCallStaticMethodJNI(X64Context context, X64PreservedRegister classReg, X64PreservedRegister methodId,
                                        Register[] args, Register returnVal) {

        // 3 options for the method call, but will use the first one
        // %result = CallNonVirtual<type>Method(JNIEnv, obj, class, methodID, ...);

        // arg 1
        context.loadJNI1();

        // arg 2
        context.addInstruction(
            new MovePseudoRegToReg(
                classReg,
                context.argumentRegister(2)
            )
        );

        // arg 3
        context.addInstruction(
            new MovePseudoRegToReg(
                methodId,
                context.argumentRegister(3)
            )
        );

        // arg 4+, the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.length; i++) {
            context.addInstruction(
                new MovePseudoRegToReg(
                    args[i].toX64(),
                    context.argumentRegister(i + 4)
                )
            );
        }

        addCallJNI(context, getCallStaticMethodOffset(returnVal.getType()), returnVal.toX64());
    }
}
