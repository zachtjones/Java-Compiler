import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit.ScenarioTest;
import helper.ProcessRunner;
import main.JavaCompiler;
import main.OutputDirs;
import org.junit.Test;

import java.io.File;

import static main.FileReader.readResourcesFile;
import static org.assertj.core.api.Assertions.assertThat;

public class TestCompiler extends ScenarioTest<GivenInputProgram, WhenItCompilesAndRuns, ThenExpectedOutputIs> {

    @Test
    public void testValidPrograms() {
        given()
            .theInputProgram("HelloWorld.java");

        when()
            .theProgramCompilesSuccessfully()
            .and()
            .itRuns();

        then()
            .theOutputsMatchFile("HelloWorld.txt")
            .and()
            .theErrorMatchesFile("HelloWorld.txt")
            .and()
            .theExitCodeIs(0);
    }


}

class GivenInputProgram extends Stage<GivenInputProgram> {

    @ProvidedScenarioState
    private String fileName;


    public GivenInputProgram theInputProgram(String fileName) {
        this.fileName = fileName;
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


    public WhenItCompilesAndRuns theProgramCompilesSuccessfully() {
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
        final String contents = readResourcesFile("output/" + name);
        assertThat(output).isEqualToIgnoringNewLines(contents);
        return self();
    }

    public ThenExpectedOutputIs theErrorMatchesFile(String name) {
        final String contents = readResourcesFile("error/" + name);
        assertThat(errOutput).isEqualToIgnoringNewLines(contents);
        return self();
    }

    public ThenExpectedOutputIs theExitCodeIs(int value) {
        assertThat(exitCode).isEqualTo(value);
        return self();
    }
}
