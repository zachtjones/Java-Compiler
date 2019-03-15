package x64.jni;

import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

import static x64.jni.JNIOffsets.ALLOC_OBJECT;

public interface AllocObjectJNI extends CallJNIMethod {

    /**
     * Adds the necessary instructions required to allocate a java object.
     * @param context The X64 context that the code gets added to.
     * @param classReg The java class, obtained via FindClass.
     * @return The newly allocated preserved register holding a reference to the JNI class
     */
    default X64PseudoRegister addAllocObjectJNICall(X64Context context, X64PseudoRegister classReg) {

        // mov %javaEnvOne, %arg1
        context.loadJNI1();

        // classReg, %arg2
        context.addInstruction(
            new MovePseudoToReg(
                classReg,
                context.argumentRegister(2)
            )
        );

        // call JNI find class with the arguments, storing result in object register
        final X64PseudoRegister objectReg = context.getNextQuadRegister();

        addCallJNI(context, ALLOC_OBJECT, objectReg);

        return objectReg;
    }
}
