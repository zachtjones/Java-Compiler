package x64.jni;

import x64.X64Function;
import x64.instructions.MoveInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64NativeRegister;
import x64.operands.X64RegisterOperand;

import static x64.jni.JNIOffsets.GET_OBJECT_CLASS;

public interface GetObjectClassJNI extends CallJNIMethod {

    /**
     * Adds the code returned = GetObjectClass(JNI, obj);
     * @param function The x64 function to add the code to
     * @param object The x64 register that holds the reference to the object
     * @return A new x64 register that holds the object's class.
     */
    default X64RegisterOperand addGetObjectClass(X64Function function, X64RegisterOperand object) {
        // result = JNIEnv -> GetObjectClass(JNIEnv, obj);

        // JNIEnv -> arg1
        function.loadJNI1();

        // object -> arg2
        function.addInstruction(
            new MoveInstruction(
                object,
                X64NativeRegister.RSI
            )
        );

        X64RegisterOperand result = function.getNextQuadRegister();

        addCallJNI(function, GET_OBJECT_CLASS, result);

        return result;
    }
}
