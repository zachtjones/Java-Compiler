package x64;

public class X64File {

    private final String fileName;

    /** Creates an initially empty x64 assembly file, given the name
     * @param name The java class/interface/enum name, ex: java/lang/String
     */
    public X64File(String name) {
        // use the java -> native pattern, escaping _ in package names
        fileName = name.replace("_", "_1").replace('/', '_') + ".s";
    }

    /** Gets the file name that this assembly file should be. (Not including the directory) */
    public String getFileName() {
        return fileName;
    }
}
