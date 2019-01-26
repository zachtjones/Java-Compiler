package main;

import helper.ProcessRunner;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TestGCC {

	@Test
	void verifyGCCWorks() throws Exception {
		// compile a sample c file to verify that gcc works,
		// and it's not the assembly generated for a specific platform

		String cFile = FileReader.readResourcesFile("shared-library.c");
		FileWriter.writeToOutput(OutputDirs.ASSEMBLED, "shared-library.c", cFile);

		ProcessRunner gcc = new ProcessRunner(
			"gcc",
			"-fPIC",
			"shared-library.c",
			"-shared",
			"-o", System.mapLibraryName("Main")
		);
		gcc.setDirectory(new File(OutputDirs.ASSEMBLED.location));
		ProcessRunner.ProcessResult result = gcc.run();

		System.out.println(result.getOutput());
		System.err.println(result.getError());

		assertThat(result.getExitCode()).as("exit code for gcc is 0").isEqualTo(0);

		// move the Main.java class over and compile it
		String mainJava = FileReader.readResourcesFile("Main.java");
		FileWriter.writeToOutput(OutputDirs.ASSEMBLED, "Main.java", mainJava);

		ProcessRunner javac = new ProcessRunner("javac", "Main.java");
		javac.setDirectory(new File(OutputDirs.ASSEMBLED.location));
		ProcessRunner.ProcessResult result2 = javac.run();

		System.out.println(result2.getOutput());
		System.err.println(result2.getError());

		assertThat(result2.getExitCode()).as("exit code for javac is 0").isEqualTo(0);

		// java -Djava.library.path=. Main
		ProcessRunner java = new ProcessRunner("java", "-Djava.library.path=.", "Main");
		java.setDirectory(new File(OutputDirs.ASSEMBLED.location));
		ProcessRunner.ProcessResult javaResults = java.run();

		System.out.println(javaResults.getOutput());
		System.err.println(javaResults.getError());

		assertThat(javaResults.getExitCode()).as("exit code for java is 0").isEqualTo(0);
	}
}
