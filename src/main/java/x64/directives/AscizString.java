package x64.directives;

public class AscizString extends Directive {

    private String content;

    public AscizString(String content) {
        this.content = content;
    }

    @Override
    public String assemblyRepresentation() {
        return String.format(".asciz \"%s\"", content);
    }

}
