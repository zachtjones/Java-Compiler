package x64.instructions;

import x64.Instruction;
import x64.allocation.RegistersUsed;
import x64.operands.SourceOperand;
import x64.operands.X64NativeRegister;
import x64.operands.X64PreservedRegister;
import x64.operands.X64RegisterOperand;

import java.util.Map;

/** Represents a call to a function pointer, callq *%rbx for example */
public class CallFunctionPointerInstruction implements Instruction {
    private SourceOperand temp;

    public CallFunctionPointerInstruction(SourceOperand temp) {
        this.temp = temp;
    }

    @Override
    public boolean isCalling() {
        return true;
    }

    @Override
    public void markRegisters(int i, RegistersUsed usedRegs) {
        temp.markUsed(i, usedRegs);
    }

    @Override
    public void allocateRegisters(Map<X64PreservedRegister, X64NativeRegister> mapping) {
        temp.swapOut(mapping);
    }

    @Override
    public String toString() {
        return "\tcall *" + temp.toString();
    }
}
