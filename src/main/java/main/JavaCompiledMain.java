package main;

import helper.CompileException;
import x64.X64File;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static main.FileReader.readResourcesFile;

public class JavaCompiledMain {
    private final String content;

    JavaCompiledMain() {
        content = readResourcesFile("Main.java");
    }

    /** Compiles the Main class for java, saving it to the temp folder. */
    public void compile() throws IOException, InterruptedException, CompileException {

        OutputDirs.ASSEMBLED.createDir();
        final String outputMainJava = OutputDirs.ASSEMBLED.location + "Main.java";
        PrintWriter pw = new PrintWriter(outputMainJava);
        pw.println(content);

        // `javac Main.java`
        Process p = Runtime.getRuntime().exec("javac",
                new String[]{outputMainJava}, new File(OutputDirs.ASSEMBLED.location));
        p.waitFor();

        // copy the assembly bridge file
        OutputDirs.ASSEMBLY.createDir();
        PrintWriter assemblyBridge = new PrintWriter(OutputDirs.ASSEMBLY.location + "Main.s");
        assemblyBridge.println(readResourcesFile("Main-x86_64.s"));

        pw.flush();
        pw.close();

        // Signature for the main method defined in the program
        // JNIEXPORT void JNICALL Java_Main_mainMethod(JNIEnv *, jclass, jobjectArray);

        // 64-bit architecture:
        //  int = jint, long = jlong, unsigned char = jboolean, unsigned short = jchar,
        //  short = jshort, float = jfloat, double = jdouble
        //  int = jsize

        // System.loadLibrary("Main"); library is named: -->
        //  - Mac OS -- libMain.dylib
        //  - Linux -- libMain.so - untested
        //  - Windows -- Main.dll - untested

        // System.getProperty("os.arch") -> "x86_64" or "amd64"
        // System.getProperty("os.name") -> "Mac OS X"
        // System.getProperty("os.version") -> "10.14"

    }
}
