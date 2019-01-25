package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import helper.ClassLookup;
import helper.CompileException;
import helper.ProcessRunner;
import helper.Types;
import javaLibrary.JavaLibraryLookup;
import tree.*;
import intermediate.*;
import x64.X64File;

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
			return JavaLibraryLookup.getLibraryFile(fullyQualifiedName, fileName, line);
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

	public static void main(String[] args) throws FileNotFoundException, ParseException, CompileException {
		if (args.length < 1) {
			usage();
		}
		String file = args[0];
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
			// write to output
			FileWriter.writeToOutput(OutputDirs.INTERMEDIATE, f.getName(), f.toString());
		}

		InterFile mainClass = null;

		final ArrayList<Types> arg = new ArrayList<>();
		arg.add(Types.arrayOf(Types.STRING));

		for (InterFile f : files) {
			if (f.getReturnType("main", arg) != null) {
				mainClass = f;
			}
		}

		// enforce there is a `static void main(String[] args)`
		if (mainClass == null) {
			throw new CompileException("There is not a main method in the files given.", file, 0);
		}

		// compile to the native code - starting with the java -> native class
		final JavaCompiledMain entryCode = new JavaCompiledMain(mainClass);
		entryCode.compile();

		// step 1: compile the code down to assembly files
		// the 2 supported architectures are basically the same, with almost identical assembly,
		//  if there are any differences between them, the java library probably handles most of that difference
		//  meaning very little assembly code will be different
		final String arch = System.getProperty("os.arch");
		if (!Arrays.asList("x86_64", "amd64").contains(arch)) {
			throw new CompileException(
				"Unsupported computer architecture. Currently only supports x86_64 & amd64.", "", -1);
		}

		final ProcessRunner gcc = new ProcessRunner(
			"gcc",
			"-fPIC", // force position independent code (for shared library)
			"-shared",
			"--enable-shared",
			"--save-temps",
			"-o",
			"../assembled/" + entryCode.getLibraryName(),
			"Main.s" // other ones added to this list
		);

		// convert files from intermediate to assembly & add argument to gcc
		for (InterFile f : cache.values()) {
			X64File compiled = f.compileX64();
			FileWriter.writeToOutput(OutputDirs.PSEUDO_ASSEMBLY, compiled.getFileName(), compiled.toString());

			compiled.allocateRegisters();
			FileWriter.writeToOutput(OutputDirs.ASSEMBLY, compiled.getFileName(), compiled.toString());
			gcc.addArg(compiled.getFileName());
		}

		gcc.setDirectory(new File(OutputDirs.ASSEMBLY.location));

		final ProcessRunner.ProcessResult gccResult = gcc.run();
		if (gccResult.getExitCode() != 0) {
			System.err.println("gcc exit code: " + gccResult.getExitCode());
			System.err.println("gcc error output: '" + gccResult.getError() + "'");
			System.err.println("gcc output: '" + gccResult.getOutput() + "'");
			throw new CompileException("gcc failed, error is: " + gccResult.getError(), "", -1);
		}

		System.out.println("Done, results are in the temp/assembled folder.");
		System.out.println("Invoke the program: `java -Djava.library.path=\".\" Main` from the temp folder");
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
