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

    public void createDir() throws CompileException {
        boolean good = new File(location).mkdirs();
        if (!good)
            throw new CompileException("Error, can't create the temporary directory: " + location, "", -1);
    }
}
