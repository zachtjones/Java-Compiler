package x64.jni;

import x64.X64Context;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64RegisterOperand;

import static x64.allocation.CallingConvention.argumentRegister;
import static x64.jni.JNIOffsets.GET_OBJECT_CLASS;

public interface GetObjectClassJNI extends CallJNIMethod {

    /**
     * Adds the code returned = GetObjectClass(JNI, obj);
     * @param context The x64 context to add the code to
     * @param object The x64 register that holds the reference to the object
     * @return A new x64 register that holds the object's class.
     */
    default X64RegisterOperand addGetObjectClass(X64Context context, X64RegisterOperand object) {
        // result = JNIEnv -> GetObjectClass(JNIEnv, obj);

        // JNIEnv -> arg1
        context.loadJNI1();

        // object -> arg2
        context.addInstruction(
            new MoveInstruction(
                object,
                argumentRegister(2)
            )
        );

        X64RegisterOperand result = context.getNextQuadRegister();

        addCallJNI(context, GET_OBJECT_CLASS, result);

        return result;
    }
}
