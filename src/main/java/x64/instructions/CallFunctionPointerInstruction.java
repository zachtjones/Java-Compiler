package x64.instructions;

import x64.Instruction;
import x64.operands.X64PreservedRegister;

/** Represents a call to a function pointer, callq *%rbx for example */
public class CallFunctionPointerInstruction implements Instruction {
    private X64PreservedRegister temp;

    public CallFunctionPointerInstruction(X64PreservedRegister temp) {
        this.temp = temp;
    }

    @Override
    public boolean isCalling() {
        return true;
    }

    @Override
    public String toString() {
        return "\tcallq *" + temp.assemblyRep();
    }
}
