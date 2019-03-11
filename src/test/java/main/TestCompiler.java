package main;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import com.tngtech.jgiven.junit5.JGivenExtension;
import helper.ProcessRunner;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( JGivenExtension.class )
class TestCompiler extends ScenarioTest<GivenInputProgram, WhenItCompilesAndRuns, ThenExpectedOutputIs> {

    @ParameterizedTest
    @MethodSource("programList")
    void testValidPrograms(String inputProgramName, int exitCode, String output, String errorOut)
        throws Exception {

        given()
            .theInputProgram(inputProgramName);

        when()
            .theProgramCompilesSuccessfully()
            .and()
            .itRuns();

        then()
            .theExitCodeIs(exitCode)
            .and()
            .theOutputsMatches(output)
            .and()
            .theErrorMatches(errorOut);
    }

    /// names of the programs to run
    static Stream<Arguments> programList() {
        return Stream.of(
            Arguments.of("HelloWorld", 0, "Hello, World!\n", ""),
            Arguments.of("BasicClass", 0, "b is: 5\na is: 1\n", ""),
            Arguments.of("BasicClass2", 0, "12345\n", ""),
            Arguments.of("OutOfRegisters", 0, "91\n", ""),
            Arguments.of("PrintALot", 0, "4a6bcde63\n", ""),
            Arguments.of("IfStatements", 0, "a is 6\na is 7\na is positive\na is negative", "")
        );
    }


}

class GivenInputProgram extends Stage<GivenInputProgram> {

    @ProvidedScenarioState
    private String fileName;


    @SuppressWarnings("UnusedReturnValue") // used by jGiven
    GivenInputProgram theInputProgram(String fileName) {
        this.fileName = "src/main/resources/test-programs/" + fileName + ".java";
        return self();
    }
}

class WhenItCompilesAndRuns extends Stage<WhenItCompilesAndRuns> {

    @ExpectedScenarioState
    private String fileName;

    @ProvidedScenarioState
    private String output;

    @ProvidedScenarioState
    private String errOutput;

    @ProvidedScenarioState
    private int exitCode;


    WhenItCompilesAndRuns theProgramCompilesSuccessfully() throws Exception {
        String[] file = { fileName };
        JavaCompiler.main(file);
        return self();
    }

    @SuppressWarnings("UnusedReturnValue") // used by jGiven
    WhenItCompilesAndRuns itRuns() {
        ProcessRunner runner = new ProcessRunner("java", "-Djava.library.path=.", "Main");
        runner.setDirectory(new File(OutputDirs.ASSEMBLED.location));
        ProcessRunner.ProcessResult results = runner.run();
        output = results.getOutput();
        errOutput = results.getError();
        exitCode = results.getExitCode();
        return self();
    }
}

class ThenExpectedOutputIs extends Stage<ThenExpectedOutputIs> {

    @ExpectedScenarioState
    private String output;

    @ExpectedScenarioState
    private String errOutput;

    @ExpectedScenarioState
    private int exitCode;

    ThenExpectedOutputIs theOutputsMatches(String content) {
        assertThat(output).isEqualToNormalizingNewlines(content);
        return self();
    }

    @SuppressWarnings("UnusedReturnValue") // used by jGiven
    ThenExpectedOutputIs theErrorMatches(String content) {
        assertThat(errOutput).isEqualToNormalizingNewlines(content);
        return self();
    }

    ThenExpectedOutputIs theExitCodeIs(int value) {
        assertThat(exitCode).as("exit code, error message: ", errOutput).isEqualTo(value);
        return self();
    }
}
