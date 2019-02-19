package main;

import helper.CompileException;
import helper.ProcessRunner;
import intermediate.InterFile;
import intermediate.InterStructure;
import intermediate.RegisterAllocator;
import org.jetbrains.annotations.NotNull;
import x64.X64Context;
import x64.X64File;
import x64.instructions.CallLabel;

import java.io.File;

import static main.FileReader.readResourcesFile;
import static main.FileWriter.writeToOutput;

public class JavaCompiledMain {
    @NotNull private final String content;
    @NotNull private final InterFile mainClass;

    JavaCompiledMain(@NotNull InterFile mainClass) {
        content = readResourcesFile("Main.java");
        this.mainClass = mainClass;
    }

    /** Compiles the Main class for java, saving it to the temp folder. */
    public void compile() throws CompileException {

        // copy out the Main.java to the assembled location
        writeToOutput(OutputDirs.ASSEMBLED, "Main.java", content);

        ProcessRunner javac = new ProcessRunner("javac", "Main.java");
        javac.setDirectory(new File(OutputDirs.ASSEMBLED.location));

        ProcessRunner.ProcessResult result = javac.run();
        if (result.getExitCode() != 0) {
            throw new CompileException("Can't create Main.java, message: " + result.getError(), "Main.java", -1);
        }

        // write the assembly bridge file
        final X64File bridgeFile = new X64File("Main", new InterStructure(false));
        final X64Context context = new X64Context(bridgeFile, new RegisterAllocator(), "mainMethod");

        context.addInstruction(
            new CallLabel(mainClass.getName(), "main")
        );

        context.addFunctionToFile();
        bridgeFile.allocateRegisters();

        FileWriter.writeToOutput(OutputDirs.ASSEMBLY, "Main.s", bridgeFile.toString());
    }

    /** Returns the library file name for "Main"
     * On Mac OS this is libMain.dylib, Windows is Main.dll */
    String getLibraryName() {
        return System.mapLibraryName("Main");
    }
}
