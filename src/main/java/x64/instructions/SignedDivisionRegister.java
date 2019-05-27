package x64.instructions;

import x64.X64InstructionSize;
import x64.operands.X64Register;

/** Represents a signed division with a register divisor, DX:AX are used for the dividend */
public class SignedDivisionRegister extends Instruction {
    public SignedDivisionRegister(X64Register register, X64InstructionSize suffix) {
        super("\tidiv" + suffix.size + " " + register.assemblyRep(suffix) );
    }
}
