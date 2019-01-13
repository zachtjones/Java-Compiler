package main;

import java.io.*;
import java.util.stream.Collectors;

public class FileReader {

    /**
     * Returns the contents of the file that is in the src/main/resources,
     * or the empty string if the file doesn't exist.
     * @param name The file's name in the resources folder.
     * @return The contents of the file, using the platform's new line (CRLF on Windows, LF on Mac)
     */
    public static String readResourcesFile(String name) {
        InputStream is = FileReader.class.getClassLoader().getResourceAsStream(name);
        if (is == null) { // resource doesn't exist, treat as empty file
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
