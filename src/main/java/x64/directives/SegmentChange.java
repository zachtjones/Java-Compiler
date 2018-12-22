package x64.directives;

import x64.Instruction;

public class SegmentChange implements Instruction {

    public static final String TEXT = "text";
    public static final String DATA = "data";

    private final String type;

    public SegmentChange(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "." + type;
    }
}
