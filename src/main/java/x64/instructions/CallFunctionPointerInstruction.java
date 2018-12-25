package x64.instructions;

import x64.Instruction;
import x64.operands.X64Register;

/** Represents a call to a function pointer, callq *%rbx for example */
public class CallFunctionPointerInstruction implements Instruction {
    private X64Register temp;

    public CallFunctionPointerInstruction(X64Register temp) {
        this.temp = temp;
    }

    @Override
    public String toString() {
        return "callq *" + temp.assemblyRep();
    }
}
