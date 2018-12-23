package x64;

public interface Instruction {

    enum Size {
        // integer ones, quad can also be pointer
        BYTE('b'), WORD('w'), LONG('l'), QUAD('q'),

        // floating-point numbers
        SINGLE('s'), DOUBLE('d'), TEN('t');

        public char size;
        Size(char suffix) {
            this.size = suffix;
        }
    }

    /** Represents how this instruction should be represented in x64 assembly */
    String toString();
}
