package x64;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class X64File {

    private final String javaClassName;
    private final String fileName;
    public ArrayList<X64Function> functions;

    /** Creates an initially empty x64 assembly file, given the name
     * @param name The java class/interface/enum name, ex: java/lang/String
     */
    public X64File(String name) {
        javaClassName = name;
        // use the java -> native pattern, escaping _ in package names
        fileName = name.replace("_", "_1").replace('/', '_') + ".s";
        functions = new ArrayList<>();
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

    @Override
    public String toString() {
        return functions.stream()
                .map(X64Function::toString)
                .collect(Collectors.joining("\n\n"));
    }

}
