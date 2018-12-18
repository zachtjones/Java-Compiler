package main;

public enum OutputDirs {
    JAVA_LIBRARY("temp/java/"),
    INTERMEDIATE("temp/intermediate/"),
    ASSEMBLY("temp/assembly/"),
    ASSEMBLED("temp/assembled");

    public final String location;

    OutputDirs(String location) {
        this.location = location;
    }
}
