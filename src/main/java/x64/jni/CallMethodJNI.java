package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

import java.util.List;

import static x64.jni.JNIOffsets.getCallMethodOffset;

public interface CallMethodJNI extends CallJNIMethod {

    /** Adds the code to JNI Call&lt;type&gt;Method */
    default void addCallMethodJNI(X64Context context, X64PseudoRegister objReg,
                                  X64PseudoRegister methodId, List<Register> args, Register returnVal) {

        // 3 options for the method call, using the first one:
        // %result = Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...);

        // arg 1
        context.loadJNI1();

        // arg 2
        context.addInstruction(
            new MovePseudoToReg(
                objReg,
                context.argumentRegister(2)
            )
        );

        // arg 3
        context.addInstruction(
            new MovePseudoToReg(
                methodId,
                context.argumentRegister(3)
            )
        );

        // arg 4+
        // the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.size(); i++) {
            context.addInstruction(
                new MovePseudoToReg(
                    args.get(i).toX64(),
                    context.argumentRegister(i + 4)
                )
            );
        }

        // Call<type>Method(JNIEnv *env, jobject obj, jmethodID methodID, ...) -> returnVal
        final JNIOffsets offset = getCallMethodOffset(returnVal.getType());

        addCallJNI(context, offset, returnVal.toX64());
    }
}
