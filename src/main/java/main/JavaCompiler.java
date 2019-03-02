package main;

import helper.CompileException;
import helper.ProcessRunner;
import intermediate.InterFile;
import javaLibrary.JavaLibraryLookup;
import org.jetbrains.annotations.NotNull;
import tree.CompilationUnit;
import x64.X64File;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
	public static InterFile parseAndCompile(@NotNull String fullyQualifiedName, @NotNull String fileName, int line)
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

			// resolves imports, compile to intermediate language
			ArrayList<InterFile> files = c.compile(newFileName);
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

		// clear the cache between iterations
		cache.clear();

		System.out.println("os.name: " + System.getProperty("os.name"));
		System.out.println("os.arch: " + System.getProperty("os.arch"));
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

		// compile to IL & put in cache
		ArrayList<InterFile> files = c.compile(file);
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

		// find main method based class
		for (InterFile f : files) {
			if (f.hasMainMethod()) {
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
			"-m64", // use 64-bit addresses
			"-shared",
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
	}

	private static void usage() {
		System.err.println("Usage: JavaCompiler <main java file> [options]");
		System.exit(1);
	}

}
