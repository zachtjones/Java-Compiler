package x64.jni;

import intermediate.Register;
import x64.X64Context;
import x64.jni.helpers.CallJNIMethod;
import x64.operands.X64PseudoRegister;
import x64.pseudo.MovePseudoToReg;

import java.util.List;

import static x64.jni.JNIOffsets.getCallNonVirtualMethodOffset;

public interface CallNonVirtualMethodJNI extends CallJNIMethod {

    /**
     * add the code for the non virtual method call
     * @param context The x64 context to add the instructions to
     * @param classReg The register that holds the reference obtained via Find/Get Class
     * @param objReg The object to pass in as 'this'
     * @param methodId The method ID obtained previously
     * @param args The program arguments to the function
     * @param returnVal Where to store the returned value
     */
    default void addCallNonVirtualMethodJNI(X64Context context, X64PseudoRegister classReg,
                                            X64PseudoRegister objReg, X64PseudoRegister methodId,
                                            List<Register> args, Register returnVal) {

        // 3 options for the method call, but will use the first one
        // %result = CallNonVirtual<type>Method(JNIEnv, obj, class, methodID, ...);

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
                classReg,
                context.argumentRegister(3)
            )
        );

        // arg 4
        context.addInstruction(
            new MovePseudoToReg(
                methodId,
                context.argumentRegister(4)
            )
        );

        // arg 5+, the actual program arguments to the function
        // insert up to the number of registers required to fill up the args
        for (int i = 0; i < args.size(); i++) {
            context.addInstruction(
                new MovePseudoToReg(
                    args.get(i).toX64(),
                    context.argumentRegister(i + 5)
                )
            );
        }

        addCallJNI(context, getCallNonVirtualMethodOffset(returnVal.getType()), returnVal.toX64());
    }
}
