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
    void testValidPrograms(String inputProgramName, String output, String errorOut)
        throws Exception {

        given()
            .theInputProgram(inputProgramName);

        when()
            .theProgramCompilesSuccessfully()
            .and()
            .itRuns();

        then()
            .theOutputsMatches(output)
            .and()
            .theErrorMatches(errorOut);
    }

    /// names of the programs to run
    private static Stream<Arguments> programList() {
        return Stream.of(
            Arguments.of("HelloWorld", "Hello, World!\n", ""),
            Arguments.of("BasicClass", "b is: 5\na is: 1\n", ""),
            Arguments.of("BasicClass2", "12345\n", ""),
            Arguments.of("OutOfRegisters", "91\n", ""),
            Arguments.of("PrintALot", "4a6bcde63\n", ""),
            Arguments.of("SimpleIfStatement", "a is 6\nalways run\nalways run\n", ""),
            Arguments.of("IfStatements", "a is 6\na is 7\na is positive\na is negative\n", ""),
            Arguments.of("SimpleWhileLoop", "123456789\n", ""),
            Arguments.of("SimpleDoWhileLoop", "123456789\n", ""),
            Arguments.of("SimpleForLoop", "123456789\n", ""),
            Arguments.of("IntermediateForLoop", "12346789\n", ""),
            Arguments.of("IntermediateWhileLoop", "12346789\n", ""),
            Arguments.of("IntermediateDoWhileLoop", "12346789\n", ""),
            Arguments.of("TwoVariableForLoop", "123456789\n", ""),
            Arguments.of("LabeledLoops", "For loop:\n" +
                                         "1, 0\n2, 0\n2, 1\n4, 0\n4, 1\n4, 2\n4, 3\n" +
                                         "Do loop:\n" +
                                         "1, 0\n2, 0\n2, 1\n4, 0\n4, 1\n4, 2\n4, 3\n" +
                                         "While loop:\n" +
                                         "1, 0\n2, 0\n2, 1\n4, 0\n4, 1\n4, 2\n4, 3\n", ""),
            Arguments.of("DataSizes", "true\n" +
                "false\n" +
                "127\n" +
                "128\n" +
                "-128\n" +
                "ab\n" +
                "-18690\n" +
                "-2147483648\n" +
                "2147483648\n", ""),
            Arguments.of("BasicArray", "[1, 2, 3, 4]\n", ""),
            Arguments.of("SimpleArray", "[1, 1, 2, 3, 5, 8, 13, 21, 34, 55]\n", ""),
            Arguments.of("ArrayLength", "143\n", ""),
            Arguments.of("TwoDimensionArray", "[X, O, O]\n[O, X, O]\n[O, O, X]\n", ""),
            Arguments.of("JaggedArray", "[0]\n[0, 0]\n[0, 0, 0]\n[0, 0, 0, 0]\n", ""),
            Arguments.of("IntegerLiterals", "16\n16\n16\n16\n16\n16\n16\n16\n16\n16\n", ""),
            Arguments.of("UnaryOperations", "-128\nfalse\n10101111000011110010101000001111\n" +
                "0\ntrue\n-1\n-20\ntrue\n-21\n20\n", ""),
            Arguments.of("Incrementing", "3\n5\n4\n4\n", ""),
            Arguments.of("Decrementing", "5\n3\n4\n4\n", "")
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

    ThenExpectedOutputIs theOutputsMatches(String content) {
        assertThat(output).isEqualToNormalizingNewlines(content);
        return self();
    }

    @SuppressWarnings("UnusedReturnValue") // used by jGiven
    ThenExpectedOutputIs theErrorMatches(String content) {
        assertThat(errOutput).isEqualToNormalizingNewlines(content);
        return self();
    }
}
