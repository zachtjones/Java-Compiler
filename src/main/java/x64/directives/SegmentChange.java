package x64.directives;

public class SegmentChange extends Directive {

    public static final String TEXT = "text";
    public static final String DATA = "data";

    private final String type;

    public SegmentChange(String type) {
        this.type = type;
    }

    @Override
    public String assemblyRepresentation() {
        return "." + type;
    }
}
