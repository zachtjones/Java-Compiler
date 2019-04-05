package x64.directives;

/** Uses a label to be used as the target of jumps, calls, and offsets. */
public class LabelInstruction extends Directive {

    public LabelInstruction(String name) {
        super(name + ":");
    }
}
