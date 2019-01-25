package x64.directives;

public class AscizString implements Directive {

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
