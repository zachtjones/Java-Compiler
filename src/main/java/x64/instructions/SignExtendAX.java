package x64.instructions;

import x64.X64InstructionSize;
import x64.pseudo.PseudoInstruction;

/** Sign extends AX to DX:AX, either 32-bit or 64-bit versions of the registers. */
public class SignExtendAX extends Instruction {
    public SignExtendAX(X64InstructionSize suffix) {
        // convert long to double or quad to octuple
        super(suffix == X64InstructionSize.LONG ? "\tcltd" : "\tcqto");
    }
}
