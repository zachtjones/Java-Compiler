package main;

import helper.CompileException;
import helper.ProcessRunner;
import intermediate.InterFile;
import x64.X64File;
import x64.X64Function;
import x64.instructions.CallClassMethod;
import x64.instructions.PopInstruction;
import x64.instructions.PushInstruction;
import x64.operands.X64NativeRegister;

import java.io.File;

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
        final X64File bridgeFile = new X64File("main");
        final X64Function bridgeFunction = new X64Function("Main", "mainMethod", 0);
        bridgeFile.addFunction(bridgeFunction);

        bridgeFunction.addInstruction(
            new PushInstruction(X64NativeRegister.RBX)
        );
        bridgeFunction.addInstruction(
            new CallClassMethod(mainClass.getName(), "main")
        );
        bridgeFunction.addInstruction(
            new PopInstruction(X64NativeRegister.RBX)
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
