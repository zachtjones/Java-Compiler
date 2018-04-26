import java.io.FileNotFoundException;
//import java.util.ArrayList;

public class JavaCompiler {

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0) {
			usage();
		}
		String file = args[0];
		JavaParser.parse(file);
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
