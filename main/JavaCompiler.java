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
	private static String rootDir;

	/** the java library source folder for *.java or *.jil files */
	private static final String javaDir = "lib/"; 

	/**
	 * Will parse and compile the file, using the cache if already done.
	 * @param fullyQualifiedName The java class name (ex: java.lang.String)
	 * @return The Intermediate file representation.
	 */
	public static InterFile parseAndCompile(String fullyQualifiedName)
			throws CompileException {

		// use the cache to save compile time
		if (cache.containsKey(fullyQualifiedName)) {
			return cache.get(fullyQualifiedName);
		}

		String filename;
		if (fullyQualifiedName.startsWith("java/")) {
			filename = javaDir + fullyQualifiedName;
		} else {
			filename = rootDir + fullyQualifiedName;
		}
		filename += ".java";

		try {
			CompilationUnit c = JavaParser.parse(filename);

			// update the class lookup tables (short names to full names)
			String packageName = c.packageName == null ? null : c.packageName.getSimpleName();
			SymbolTable classLevel = new SymbolTable(null, SymbolTable.className);
			ClassLookup lookup = new ClassLookup(filename, packageName, c.imports, classLevel);

			// resolve the imports -- placing resolved names into the global symbol table
			c.resolveImports(lookup);

			// compile and put all in the cache
			ArrayList<InterFile> files = c.compile(classLevel);
			for (int i = 0; i < files.size(); i++) {
				cache.put(files.get(i).getFileName(), files.get(i));
			}

			// find the one to return
			for (int i = 0; i < files.size(); i++) {
				if (files.get(i).getFileName().equals(fullyQualifiedName)) {
					return files.get(i);
				}
			}

		} catch (FileNotFoundException e) {
			throw new CompileException("File not found for " + fullyQualifiedName, e, filename, -1);
		} catch (ParseException e) {
			throw new CompileException("Could not parse " + fullyQualifiedName, e, 
					filename, e.currentToken.beginLine);
		} catch (IOException e) {
			throw new CompileException("I/O exception while processing " + fullyQualifiedName, 
					e, filename, -1);
		}

		// none were found
		return null;
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
				cache.put(f.getFileName(), f);
			}

			// resolve types of the IL functions to do type checking.
			for (InterFile f : files) {
				f.typeCheck();
			}

			// print out the resulting IL
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
