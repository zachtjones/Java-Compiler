package main;

import helper.CompileException;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {

    /**
     * Utility method for writing to the output directory
     * @param directory The OutputDirs constant for the directory.
     * @param name The file name to write to.
     * @param content The content to write as a string (platform default encoding).
     * @throws CompileException If there is an error writing to the file
=     */
    public static void writeToOutput(@NotNull OutputDirs directory, @NotNull String name, @NotNull String content)
            throws CompileException {

        directory.createDir();

        try {
            PrintWriter pw = new PrintWriter(directory.location + name);
            pw.println(content);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            throw new CompileException("Couldn't write to the file", e, name, -1);
        }
    }
}
