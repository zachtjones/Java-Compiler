package main;

import helper.CompileException;
import intermediate.InterFile;
import x64.X64File;
import x64.X64Function;
import x64.instructions.CallClassMethod;

import java.io.File;
import java.io.IOException;

import static main.FileReader.readResourcesFile;
import static main.FileWriter.writeToOutput;

public class JavaCompiledMain {
    private final String content;
    private final InterFile mainClass;

    JavaCompiledMain(InterFile mainClass) {
        content = readResourcesFile("Main.java");
        this.mainClass = mainClass;
    }

    /** Compiles the Main class for java, saving it to the temp folder. */
    public void compile() throws IOException, InterruptedException, CompileException {

        // copy out the Main.java to the assembled location
        writeToOutput(OutputDirs.ASSEMBLED, "Main.java", content);

        // `javac Main.java`
        Process p = Runtime.getRuntime().exec("javac Main.java",
                new String[]{}, new File(OutputDirs.ASSEMBLED.location));

        System.out.println("javac Main.java -> " + p.waitFor());

        // write the assembly bridge file
        final X64File bridgeFile = new X64File("main");
        final X64Function bridgeFunction = new X64Function("Main", "mainMethod", 0);
        bridgeFile.addFunction(bridgeFunction);

        bridgeFunction.addInstruction(
            new CallClassMethod(mainClass.getName(), "main")
        );

        bridgeFile.allocateRegisters();

        FileWriter.writeToOutput(OutputDirs.ASSEMBLY, "Main.s", bridgeFile.toString());
    }

    /** Returns the library file name for "Main"
     * On Mac OS this is libMain.dylib, Windows is Main.dll */
    String getLibraryName() {
        return System.mapLibraryName("Main");
    }
}
