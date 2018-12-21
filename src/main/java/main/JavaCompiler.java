package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import javaLibrary.JavaLibraryLookup;
import tree.*;
import intermediate.*;
import x86.X86_64File;

public class JavaCompiler {

	/** Holds all parsed & compiled files */
	private static HashMap<String, InterFile> cache = new HashMap<>();

	/** the root src folder, ending with '/' */
	private static String rootDir;

	/**
	 * Will parse and compile the file, using the cache if already done.
	 * @param fullyQualifiedName The java class name (ex: java/lang/String)
	 * @return The Intermediate file representation, or null 
	 */
	public static InterFile parseAndCompile(String fullyQualifiedName, String fileName, int line)
			throws CompileException {
		
		fullyQualifiedName = fullyQualifiedName.replace('.', '/');

		// use the cache to save compile time
		if (cache.containsKey(fullyQualifiedName)) {
			return cache.get(fullyQualifiedName);
		}

		if (fullyQualifiedName.startsWith("java/")) {
			return JavaLibraryLookup.getLibraryFile(fullyQualifiedName);
		}

		final String newFileName = rootDir + fullyQualifiedName + ".java";

		try {
			CompilationUnit c = JavaParser.parse(newFileName);

			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.primaryName;
			SymbolTable classLevel = new SymbolTable(null, SymbolTable.className);
			ClassLookup lookup = new ClassLookup(newFileName, packageName, c.imports, classLevel);

			// resolve the imports -- placing resolved names into the global symbol table
			c.resolveImports(lookup);

			// compile and put all in the cache
			ArrayList<InterFile> files = c.compile(classLevel);
			for (InterFile file : files) {
				file.typeCheck();
				cache.put(file.getName(), file);
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
				String packageFile = c.packageName.primaryName.replace('.', '/');
				rootDir = new File(file).getParent().replace(packageFile, "");
			}

			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.primaryName;
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

			InterFile mainClass = null;
			final Register argsArray = new Register(0, Register.REFERENCE, "auto-generated", -1);
			argsArray.typeFull = "java/lang/String[]";
			for (InterFile f : files) {
				if (f.getReturnType("main", new Register[]{argsArray}) != null) {
					mainClass = f;
				}
			}

			// enforce there is a `static void main(String[] args)`
			if (mainClass == null) {
				throw new CompileException("There is not a main method in the files given.", c.fileName, 0);
			}

			// compile to the native code
			final JavaCompiledMain entryCode = new JavaCompiledMain();
			entryCode.compile();

			// step 1: compile the code down to assembly files
			if (System.getProperty("os.arch").equals("x86_64")) {
				X86_64File compiledMain = entryCode.compileX86_64();
				System.out.println(compiledMain);
				for (InterFile f : files) {
					X86_64File compiled = f.compileX86_64();
					System.out.println(compiled);
				}
			} else if (System.getProperty("os.arch").equals("amd64")) {
				throw new CompileException("AMD 64 compiling not implemented yet", "", -1);
			} else {
				throw new CompileException(
						"Unsupported computer architecture. Currently only supports x86_64 & amd64.", "", -1);
			}

			// step 2: prepare the java wrapper class
			new JavaCompiledMain().compile();

			System.out.println("Done, results are in the temp folder.");
			System.out.println("Invoke the program: `java -Djava.library.path=. Main` from the temp folder");

			// gcc *.s -dynamiclib -o libMain.dylib -- command to compile all the assembly files

		} catch (ParseException e) {
			System.out.print("Syntax error at line ");
			System.out.print(e.currentToken.next.beginLine);
			System.out.println(", column " + e.currentToken.next.beginColumn);
			System.out.print("Check near: \"");
			System.out.println(e.currentToken.next.image + "\"");
		} catch (FileNotFoundException e) {
			System.out.println("Error: the file: " + e.getMessage() + " was not found.");
		} catch (CompileException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Error: interrupted while running");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: I/O exception happened while compiling.");
			e.printStackTrace();
		}
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
