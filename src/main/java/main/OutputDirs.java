package main;

import helper.CompileException;

import java.io.File;

public enum OutputDirs {
    JAVA_LIBRARY("temp/java/"),
    INTERMEDIATE("temp/intermediate/"),
    ASSEMBLY("temp/assembly/"),
    ASSEMBLED("temp/assembled/");

    public final String location;

    OutputDirs(String location) {
        this.location = location;
    }

    /**
     * Creates the directory if it doesn't exist.
     * @throws CompileException If the directory doesn't exist, and it can't be created
     */
    public void createDir() throws CompileException {
        File parent = new File("temp");
        File child = new File(location);

        if (!parent.exists() && !parent.mkdir())
            throw new CompileException("Error, can't create the temporary directory: temp", "", -1);

        if (!child.exists() && !child.mkdir())
            throw new CompileException("Error, can't create the temporary directory: " + location, "", -1);
    }
}
