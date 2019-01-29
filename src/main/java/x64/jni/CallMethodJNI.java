package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.getCallMethodOffset;

public interface CallMethodJNI extends CallJNIMethod {

    /** Adds the code to JNI Call&lt;type&gt;Method */
    default void addCallMethodJNI(X64Context context, X64RegisterOperand objReg,
                                  X64RegisterOperand methodId, Register[] args, Register returnVal) {

        // 3 options for the method call, using the first one:
        // %result = Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

        // arg 1
        context.loadJNI1();

        // arg 2
        context.addInstruction(
            new MoveInstruction(
                objReg,
                context.argumentRegister(2)
            )
        );

        // arg 3
        context.addInstruction(
            new MoveInstruction(
                methodId,
                context.argumentRegister(3)
            )
        );

        // arg 4+
        // the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.length; i++) {
            context.addInstruction(
                new MoveInstruction(
                    args[i].toX64(),
                    context.argumentRegister(i + 4)
                )
            );
        }

        // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...) -> returnVal
        final JNIOffsets offset = getCallMethodOffset(returnVal.getType());

        addCallJNI(context, offset, returnVal.toX64());
    }
}
