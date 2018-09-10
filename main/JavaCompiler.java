package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import tree.*;
import intermediate.*;

public class JavaCompiler {

	/** Holds all parsed & compiled files */
	private static HashMap<String, InterFile> cache = new HashMap<>();

	/** the root src folder, ending with '/' */
	public static String rootDir;

	/** the java library source folder for *.java or *.jil files */
	public static final String javaDir = "lib/"; 

	/**
	 * Will parse and compile the file, using the cache if already done.
	 * @param fullyQualifiedName The java class name (ex: java/lang/String)
	 * @return The Intermediate file representation, or null 
	 */
	public static InterFile parseAndCompile(String fullyQualifiedName, String fileName, int line)
			throws CompileException {

		// use the cache to save compile time
		if (cache.containsKey(fullyQualifiedName)) {
			return cache.get(fullyQualifiedName);
		}

		String newFileName;
		if (fullyQualifiedName.startsWith("java/")) {
			newFileName = javaDir + fullyQualifiedName;
		} else {
			newFileName = rootDir + fullyQualifiedName;
		}
		newFileName += ".java";

		try {
			CompilationUnit c = JavaParser.parse(newFileName);

			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.getSimpleName();
			SymbolTable classLevel = new SymbolTable(null, SymbolTable.className);
			ClassLookup lookup = new ClassLookup(newFileName, packageName, c.imports, classLevel);

			// resolve the imports -- placing resolved names into the global symbol table
			c.resolveImports(lookup);

			// compile and put all in the cache
			ArrayList<InterFile> files = c.compile(classLevel);
			for (int i = 0; i < files.size(); i++) {
				System.out.println(files.get(i).getName());
				cache.put(files.get(i).getName(), files.get(i)); 
			}
			// return the one that was in the cache
			return cache.get(fullyQualifiedName);
			
		} catch (FileNotFoundException e) {
			throw new CompileException(fullyQualifiedName + " not found while compiling",
					e, fileName, line);
			
		} catch (ParseException e) {
			throw new CompileException(fullyQualifiedName + " not able to be parsed, error at: " 
					+ newFileName + ":" + e.currentToken.beginLine
					+ "\n\treferenced", e, fileName, line);
		} catch (IOException e) {
			throw new CompileException("I/O exception while processing " + fullyQualifiedName
					+ ", referenced at", e, newFileName, -1);
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			usage();
		}
		String file = args[0];
		try {
			CompilationUnit c = JavaParser.parse(file);
			if (c.packageName == null) {
				// get the folder that the file is in
				rootDir = new File(file).getParent() + "/";
			} else {
				String packageFile = c.packageName.getSimpleName().replace('.', '/');
				rootDir = new File(file).getParent().replace(packageFile, "");
			}

			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.getSimpleName();
			SymbolTable classLevel = new SymbolTable(null, SymbolTable.className);
			ClassLookup lookup = new ClassLookup(file, packageName, c.imports, classLevel);

			// resolve the imports -- placing resolved names into the global symbol table
			c.resolveImports(lookup);

			// compile to IL & put in cache
			ArrayList<InterFile> files = c.compile(classLevel);
			for (InterFile f : files) {
				cache.put(f.getName(), f);
			}

			// resolve types of the IL functions to do type checking.
			for (InterFile f : files) {
				f.typeCheck();
			}

			// print out the resulting IL
			for (InterFile f : cache.values()) {
				PrintWriter pw = new PrintWriter("temp/" + f.getName() + ".jil");
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
