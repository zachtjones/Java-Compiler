package x64.directives;

/** Aligns the resulting output file at the current segment, padding with no-ops */
public class ByteAlignment extends Directive {

    public ByteAlignment(int alignment) {
        super(String.format(".balign %d, 0x90", alignment));
    }
}
