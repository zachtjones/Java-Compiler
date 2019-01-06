package x64.jni;

import x64.X64File;
import x64.X64Function;
import x64.instructions.LoadEffectiveAddressInstruction;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.*;

import static x64.jni.JNIOffsets.FIND_CLASS;
import static x64.operands.PCRelativeData.pointerFromLabel;

public interface FindClassJNI extends CallJNIMethod {

    /**
     * Adds the necessary instructions required to get the class into the function, returning the allocated register
     *  that holds the result.
     * @param assemblyFile The X64 file used for the data section.
     * @param function The X64 function that instructions are added into.
     * @param className The java class name to load
     * @return The newly allocated preserved register holding a reference to the JNI class
     */
    default X64RegisterOperand addFindClassJNICall(X64File assemblyFile, X64Function function, String className) {

        // mov %javaEnvOne, %arg1
        function.loadJNI1();

        // leaq NEW_STRING_REFERENCE(%rip), %arg2
        String label = assemblyFile.insertDataString(className);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                pointerFromLabel(label),
                X64NativeRegister.RSI
            )
        );

        // call JNI find class with the arguments, storing result in class register
        final X64RegisterOperand classReg = function.getNextQuadRegister();

        addCallJNI(function, FIND_CLASS, classReg);

        return classReg;
    }
}
