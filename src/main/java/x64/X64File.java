package x64;

import x64.directives.AscizString;
import x64.directives.LabelInstruction;
import x64.directives.SegmentChange;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class X64File {

    private final String javaClassName;
    private final String fileName;
    private final ArrayList<X64Function> functions;

    private final ArrayList<Instruction> dataStrings;

    private int nextDataItem = 0;

    /** Creates an initially empty x64 assembly file, given the name
     * @param name The java class/interface/enum name, ex: java/lang/String
     */
    public X64File(String name) {
        javaClassName = name;
        // use the java -> native pattern, escaping _ in package names
        fileName = name.replace("_", "_1").replace('/', '_') + ".s";
        functions = new ArrayList<>();
        dataStrings = new ArrayList<>();
        dataStrings.add(new SegmentChange(SegmentChange.DATA));
    }

    /** Gets the file name that this assembly file should be. (Not including the directory) */
    public String getFileName() {
        return fileName;
    }

    /** Gets the fully qualified java class name that this file represents. */
    public String getJavaName() {
        return javaClassName;
    }

    /** Adds the function to the list of functions */
    public void addFunction(X64Function function) {
        functions.add(function);
    }

    /** Inserts the string into the data section, this string is suitable for
     * JNI -> NewStringUTF conversion if a java/lang/String is needed.
     * @return The label that corresponds to the section */
    public String insertDataString(String dataString) {
        nextDataItem++;
        String label = "L_.str" + nextDataItem;
        dataStrings.add(new LabelInstruction(label));
        dataStrings.add(new AscizString(dataString));
        return label;
    }

    /**
     * Allocates the registers, swapping the pseudo registers for real ones
     */
    public void allocateRegisters() {
        // functions are all independent
        for (X64Function function : functions) {
            function.allocateRegisters();
        }
    }

    @Override
    public String toString() {
        return functions.stream()
                .map(X64Function::toString)
                .collect(Collectors.joining("\n\n"))
                + '\n' +
        dataStrings.stream()
                .map(Instruction::toString)
                .collect(Collectors.joining("\n"));
    }
}
