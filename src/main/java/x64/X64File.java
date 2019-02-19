package x64;

import intermediate.InterStructure;
import org.jetbrains.annotations.NotNull;
import x64.directives.*;
import x64.pseudo.PseudoInstruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class X64File {

    private final String javaClassName;
    private final String fileName;

    private final ArrayList<PseudoInstruction> dataSection;

    private final ArrayList<X64Function> functions;

    private final ArrayList<PseudoInstruction> dataStrings;

    private int nextDataItem = 0;

    /** A mapping of the Strings put into the data section to their label to load them */
    @NotNull private final Map<String, String> stringsMap = new HashMap<>();

    /** Creates an initially empty x64 assembly file, given the name
     * @param name The java class/interface/enum name, ex: java/lang/String
     */
    public X64File(String name, InterStructure staticFields) {

        dataSection = new ArrayList<>();
        dataSection.add(new SegmentChange(SegmentChange.DATA));

        staticFields.forEachMember(member -> {
            int size = staticFields.getFieldSize(member);
            dataSection.add(new ByteAlignment(size));
            dataSection.add(new LabelInstruction(SymbolNames.getFieldName(name, member)));
            dataSection.add(new SpaceDirective(size));
        });

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
    String insertDataString(String dataString) {
        // use a cache for the data strings to reduce the file size in the executable
        //  this might help with cache hits
        if (stringsMap.containsKey(dataString)) {
            return stringsMap.get(dataString);
        }
        nextDataItem++;
        String label = "L_.str" + nextDataItem;
        dataStrings.add(new LabelInstruction(label));
        dataStrings.add(new AscizString(dataString));
        // put in the cache
        stringsMap.put(dataString, label);
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
        return dataSection.stream()
                .map(PseudoInstruction::toString)
                .collect(Collectors.joining("\n"))
            + "\n\n" +
            functions.stream()
                .map(X64Function::toString)
                .collect(Collectors.joining("\n\n"))
                + '\n' +
            dataStrings.stream()
                .map(PseudoInstruction::toString)
                .collect(Collectors.joining("\n"))
            + '\n' +
            StackMarkings.instance.toString();
    }
}
