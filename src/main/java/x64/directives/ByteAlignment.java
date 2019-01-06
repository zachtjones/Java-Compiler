package x64.directives;

public class ByteAlignment extends Directive {

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
