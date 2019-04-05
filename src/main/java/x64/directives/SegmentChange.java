package x64.directives;

/** Represents a change of segment to the one provided. */
public class SegmentChange extends Directive {

    public static final String TEXT = "text";
    public static final String DATA = "data";

    public SegmentChange(String type) {
        super("." + type);
    }

}
