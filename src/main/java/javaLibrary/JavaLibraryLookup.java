package javaLibrary;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static main.FileReader.readResourcesFile;

/***
 * This class is used to look up information from the java standard library.
 */
public class JavaLibraryLookup {

    private static final String[] lines = readResourcesFile("javaLookup.txt").split("\n");

    static {
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace('/', '.');
        }
    }

    /** Returns the list of fully qualified names of the classes, enums, and interfaces in the package. */
    public static List<String> getClassesInPackage(String packageName) {
        return Arrays.stream(lines)
                .filter(line -> line.startsWith(packageName))
                .collect(Collectors.toList());
    }
}
