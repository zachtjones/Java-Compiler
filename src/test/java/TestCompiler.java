import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import com.tngtech.jgiven.junit5.JGivenExtension;
import helper.ProcessRunner;
import main.JavaCompiler;
import main.OutputDirs;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static main.FileReader.readResourcesFile;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith( JGivenExtension.class )
public class TestCompiler extends ScenarioTest<GivenInputProgram, WhenItCompilesAndRuns, ThenExpectedOutputIs> {

    @ParameterizedTest
    @MethodSource("programList")
    public void testValidPrograms(String inputProgram) throws Exception {
        given()
            .theInputProgram(inputProgram);

        when()
            .theProgramCompilesSuccessfully()
            .and()
            .itRuns();

        then()
            .theExitCodeIs(0)
            .and()
            .theOutputsMatchFile(inputProgram)
            .and()
            .theErrorMatchesFile(inputProgram);
    }

    /// names of the programs to run
    public static Stream<String> programList() {
        return Stream.of(
            "HelloWorld"
        );
    }


}

class GivenInputProgram extends Stage<GivenInputProgram> {

    @ProvidedScenarioState
    private String fileName;


    public GivenInputProgram theInputProgram(String fileName) {
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


    public WhenItCompilesAndRuns theProgramCompilesSuccessfully() throws Exception {
        String[] file = { fileName };
        JavaCompiler.main(file);
        return self();
    }

    public WhenItCompilesAndRuns itRuns() {
        ProcessRunner runner = new ProcessRunner("java", "\"-Djava.library.path=.\"", "Main");
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

    public ThenExpectedOutputIs theOutputsMatchFile(String name) {
        final String contents = readResourcesFile("test-output/" + name + ".txt");
        assertThat(output).isEqualToNormalizingNewlines(contents);
        return self();
    }

    public ThenExpectedOutputIs theErrorMatchesFile(String name) {
        final String contents = readResourcesFile("test-error/" + name + ".txt");
        assertThat(errOutput).isEqualToNormalizingNewlines(contents);
        return self();
    }

    public ThenExpectedOutputIs theExitCodeIs(int value) {
        assertThat(exitCode).isEqualTo(value);
        return self();
    }
}
