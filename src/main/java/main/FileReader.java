package main;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.stream.Collectors;

public class FileReader {

    /**
     * Returns the contents of the file that is in the src/main/resources,
     * or the empty string if the file doesn't exist.
     * @param name The file's name in the resources folder.
     * @return The contents of the file, using the platform's new line (CRLF on Windows, LF on Mac)
     */
    @NotNull
    public static String readResourcesFile(@NotNull String name) {
        InputStream is = FileReader.class.getClassLoader().getResourceAsStream(name);
        if (is == null) { // resource doesn't exist, treat as empty file
            return "";
        }
        // actually have to read character by character because we need to know if the file ends in a new line
        //  or not, but we don't need the specific characters
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines().collect(Collectors.joining("\n"));
    }
}
