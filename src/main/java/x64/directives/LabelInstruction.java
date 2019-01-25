package x64.directives;

public class LabelInstruction implements Directive {
    private final String name;

    public LabelInstruction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + ":";
    }
}
