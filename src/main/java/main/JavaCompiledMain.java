package main;

import helper.CompileException;

import java.io.File;
import java.io.IOException;

import static main.FileReader.readResourcesFile;
import static main.FileWriter.writeToOutput;

public class JavaCompiledMain {
    private final String content;

    JavaCompiledMain() {
        content = readResourcesFile("Main.java");
    }

    /** Compiles the Main class for java, saving it to the temp folder. */
    public void compile() throws IOException, InterruptedException, CompileException {

        // copy out the Main.java to the assembled location
        writeToOutput(OutputDirs.ASSEMBLED, "Main.java", content);

        // `javac Main.java`
        Process p = Runtime.getRuntime().exec("javac Main.java",
                new String[]{}, new File(OutputDirs.ASSEMBLED.location));
        p.waitFor();
        System.out.println("javac Main.java -> " + p.exitValue());

        // copy the assembly bridge file
        writeToOutput(OutputDirs.ASSEMBLY, "Main.s", readResourcesFile("Main-x86_64.s"));

        // Signature for the main method defined in the program
        // JNIEXPORT void JNICALL Java_Main_mainMethod(JNIEnv *, jclass, jobjectArray);

        // 64-bit architecture:
        //  int = jint, long = jlong, unsigned char = jboolean, unsigned short = jchar,
        //  short = jshort, float = jfloat, double = jdouble
        //  int = jsize

        // System.getProperty("os.arch") -> "x86_64" or "amd64"
        // System.getProperty("os.name") -> "Mac OS X"
        // System.getProperty("os.version") -> "10.14"

    }

    /** Returns the library file name for "Main"
     * On Mac OS this is libMain.dylib, Windows is Main.dll */
    String getLibraryName() {
        return System.mapLibraryName("Main");
    }
}
