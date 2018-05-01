import java.io.FileNotFoundException;
import tree.*;

public class JavaCompiler {

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
		}
		String file = args[0];
		try {
			CompilationUnit c = JavaParser.parse(file);
			System.out.println("File passed. Syntax is valid.");
			// next task
		} catch (ParseException e) {
            System.out.print("Syntax error at line ");
            System.out.print(e.currentToken.next.beginLine);
            System.out.println(", column " + e.currentToken.next.beginColumn);
            System.out.print("Check near: \"");
            System.out.println(e.currentToken.next.image + "\"");
        } catch (FileNotFoundException e) {
        	System.out.println("Error: the input file was not found.");
        }
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
class T {
	public int m()[] { // this is valid
		int[] n = {,};
		return n;
	}
}
