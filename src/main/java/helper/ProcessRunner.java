package helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to create a process, run it, and get the results.
 */
public class ProcessRunner {
    private final List<String> args;

    private File directory;

    /** Creates a process runner, with the first one being the program to run, the rest being the arguments to
     * the running process.
     * @param args A list, the first of which is the process/file to run, followed by the arguments */
    public ProcessRunner(String... args) {
        this.args = new ArrayList<>();
        this.args.addAll(Arrays.asList(args));
    }

    /**
     * Adds another argument to the process that will be run.
     * @param arg The String argument.
     */
    public void addArg(String arg) {
        this.args.add(arg);
    }

    /**
     * Sets the working directory of the current process.
     * @param directory The file that is the directory to run.
     */
    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * Runs the process, returning a class holding results.
     * @return The class holding the String from output stream, error stream, and exit code.
     */
    public ProcessResult run() {
        System.out.println("running: " + args.toString());
        return new ProcessResult(args, directory);
    }

    public static class ProcessResult {
        private final StringBuilder out;
        private final StringBuilder err;
        private int exitCode;

        ProcessResult(List<String> args, File currentDirectory) {
            out = new StringBuilder();
            err = new StringBuilder();

            final ProcessBuilder builder = new ProcessBuilder(args);

            if (currentDirectory != null)
                builder.directory(currentDirectory);

            try {
                final Process p = builder.start();

                final Thread outThread = new Thread(() -> runWith(p.getInputStream(), out));
                outThread.start();

                final Thread errThread = new Thread(() -> runWith(p.getErrorStream(), err));
                errThread.start();

                outThread.join();
                errThread.join();

                exitCode = p.waitFor();
            } catch (InterruptedException | IOException e) {
                exitCode = Integer.MIN_VALUE;
            }
            System.out.println(out.toString());
            System.err.println(err.toString());
        }

        /** helper method to capture all the result from a process output,
         * filling in result with each line.
         * Note that the lines are filled in with the \n character */
        private void runWith(InputStream processOutput, StringBuilder result) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(processOutput))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                    result.append('\n');
                }
            } catch (IOException e) {
                /* doesn't matter if this happens, there won't be an error reading from this type of stream */
            }
        }

        public String getOutput() {
            return out.toString();
        }

        public String getError() {
            return err.toString();
        }

        public int getExitCode() {
            return exitCode;
        }
    }
}
