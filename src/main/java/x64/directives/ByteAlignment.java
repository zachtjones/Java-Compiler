package x64.directives;

import x64.Instruction;

public class ByteAlignment implements Instruction {

    private final int alignment;

    public ByteAlignment(int alignment) {
        this.alignment = alignment;
    }

    @Override
    public String toString() {
        // fill with the NOP instruction (0x90)
        return String.format(".balign %d, 0x90", alignment);
    }
}
