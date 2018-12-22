package x64;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class X64File {

    private final String fileName;
    public ArrayList<Instruction> instructions;

    /** Creates an initially empty x64 assembly file, given the name
     * @param name The java class/interface/enum name, ex: java/lang/String
     */
    public X64File(String name) {
        // use the java -> native pattern, escaping _ in package names
        fileName = name.replace("_", "_1").replace('/', '_') + ".s";
        instructions = new ArrayList<>();
    }

    /** Gets the file name that this assembly file should be. (Not including the directory) */
    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return instructions.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
