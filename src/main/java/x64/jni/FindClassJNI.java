package x64.jni;

import x64.X64Context;
import x64.pseudo.LoadEffectiveAddressRIPPseudo;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.*;

import static x64.jni.JNIOffsets.FIND_CLASS;
import static x64.operands.RIPRelativeData.pointerFromLabel;

public interface FindClassJNI extends CallJNIMethod {

    /**
     * Adds the necessary instructions required to get the class into the function, returning the allocated register
     *  that holds the result.
     * @param context The X64 context that the code gets added to.
     * @param className The java class name to load
     * @return The newly allocated preserved register holding a reference to the JNI class
     */
    default X64PreservedRegister addFindClassJNICall(X64Context context, String className) {

        // mov %javaEnvOne, %arg1
        context.loadJNI1();

        // leaq NEW_STRING_REFERENCE(%rip), %arg2
        String label = context.insertDataString(className);
        context.addInstruction(
            new LoadEffectiveAddressRIPPseudo(
                pointerFromLabel(label),
                context.argumentRegister(2)
            )
        );

        // call JNI find class with the arguments, storing result in class register
        final X64PreservedRegister classReg = context.getNextQuadRegister();

        addCallJNI(context, FIND_CLASS, classReg);

        return classReg;
    }
}
