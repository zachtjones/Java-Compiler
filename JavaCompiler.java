import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import helper.ClassLookup;
import helper.CompileException;
import tree.*;
import intermediate.*;

public class JavaCompiler {

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
		}
		String file = args[0];
		try {
			CompilationUnit c = JavaParser.parse(file);
			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.getSimpleName();
			ClassLookup lookup = new ClassLookup(file, packageName, c.imports);
			// resolve the imports
			c.resolveImports(lookup);
			
			System.out.println("Symbol table: ");
			
			// next task - compile -- print out the table.
			ArrayList<InterFile> files = c.compile();
			for (InterFile f : files) {
				PrintWriter pw = new PrintWriter(f.getFileName());
				pw.println(f.toString());
				pw.flush();
				pw.close();
			}
		} catch (ParseException e) {
            System.out.print("Syntax error at line ");
            System.out.print(e.currentToken.next.beginLine);
            System.out.println(", column " + e.currentToken.next.beginColumn);
            System.out.print("Check near: \"");
            System.out.println(e.currentToken.next.image + "\"");
        } catch (FileNotFoundException e) {
        	System.out.println("Error: the input file was not found.");
        } catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (CompileException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
