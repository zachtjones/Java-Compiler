package x64.jni;

import x64.X64File;
import x64.X64Function;
import x64.instructions.CallFunctionPointerInstruction;
import x64.instructions.LoadEffectiveAddressInstruction;
import x64.instructions.MoveInstruction;
import x64.operands.RegisterRelativePointer;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;

import static x64.JNIOffsets.FIND_CLASS;
import static x64.operands.PCRelativeData.pointerFromLabel;

public interface FindClassJNI {

    /**
     * Adds the necessary instructions required to get the class into the function, returning the allocated register
     *  that holds the result.
     * @param assemblyFile The X64 file used for the data section.
     * @param function The X64 function that instructions are added into.
     * @param className The java class name to load
     * @return The newly allocated preserved register holding a reference to the JNI class
     */
    default X64PreservedRegister addFindClassJNICall(X64File assemblyFile, X64Function function, String className) {
        // leaq NEW_STRING_REFERENCE(%rip), %arg2
        String label = assemblyFile.insertDataString(className);
        function.addInstruction(
            new LoadEffectiveAddressInstruction(
                pointerFromLabel(label),
                X64NativeRegister.RSI
            )
        );

        // mov %javaEnvOne, %arg1
        function.loadJNI1();

        // mov FindClass_Offset(%javaEnvOne), %temp
        final X64PreservedRegister temp = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
        function.addInstruction(
            new MoveInstruction(
                new RegisterRelativePointer(FIND_CLASS.getOffset(), function.javaEnvPointer),
                temp
            )
        );

        // call *%temp
        function.addInstruction(
            new CallFunctionPointerInstruction(
                temp
            )
        );

        // mov %result, %class storage register
        final X64PreservedRegister classReg = X64PreservedRegister.newTempQuad(function.getNextFreeRegister());
        function.addInstruction(
            new MoveInstruction(
                X64NativeRegister.RAX,
                classReg
            )
        );
        return classReg;
    }
}
