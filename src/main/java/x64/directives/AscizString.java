package x64.directives;

import x64.Instruction;

public class AscizString implements Instruction {

    private String content;

    public AscizString(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        // fill with the NOP instruction (0x90)
        return String.format(".asciz \"%s\"", content);
    }

}
