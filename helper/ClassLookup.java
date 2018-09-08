package helper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import tree.ImportNode;
import tree.NameNode;
import tree.SymbolTable;

public class ClassLookup {
	private static boolean lookedUpJava = false;
	private static HashMap<String, String> items = new HashMap<String, String>();
	private static HashMap<String, String> javaItems = new HashMap<String, String>();

	private SymbolTable syms;
	
	/**
	 * Loads the imports to reference the proper items.
	 * @param fileName The filename of the file currently being parsed.
	 * @param packageName The package name of the file currently being parsed,
	 *  or null.
	 * @param imports The ArrayList of ImportNodes that are declared in the final.
	 * @param classLevel The class level symbol table (from imports)
	 */
	public ClassLookup(String fileName, String packageName, ArrayList<ImportNode> imports, SymbolTable classLevel) {
		syms = classLevel;
		
		File thisFile = new File(fileName);
		// automatically import the files in the same directory / package
		// if default package (null), fully qualified == short name
		if (packageName != null) {
			File parent = thisFile.getParentFile();
			for (File f : parent.listFiles()) {
				String name = f.getName(); // just the name.extension
				String justName = f.getName().replaceAll(".java", "");
				if (name.endsWith(".java")) {
					items.put(justName, packageName + "." + name);
				}
			}
		}

		// import the files referenced from imports
		// note name.* only imports the public things (java language spec)
		// again, get the file names
		// always import java.lang.*
		ImportNode javaLang = new ImportNode(fileName, -1);
		javaLang.isAll = true;
		javaLang.name = new NameNode(fileName, -1);
		javaLang.name.primaryName = "java.lang";

	}

	/** Gets the full name from the short name representation.
	 * @param shortName the short name of this class/interface/enum
	 * @throws IOException A network is used to lookup the java all 
	 * classes for the first time.
	 * If there is an issue with this lookup, this exception is thrown.
	 * @throws CompileException If there is an error compiling.
	 * */
	public String getFullName(String shortName, String fileName, int line)
			throws IOException, CompileException {
		
		if (items.containsKey(shortName)) {
			return items.get(shortName);
		}
		// check the java.*** stuff for it
		String javaLookup = lookupJavaName(shortName, fileName, line);
		if (javaLookup != null) return javaLookup;
		
		// return the short name, assume default package;
		return shortName;
	}

	/** Looks up the java name. Note this might require a network connection,
	 * and the java name could be java.util.* or javax.* or similar classes.
	 * @param fileName The name of the file being compiled.
	 * @param fileLine The line of the current expression being compiled.
	 * @throws CompileException If there is a compile exception.
	 * */
	private String lookupJavaName(String shortName, String fileName, int fileLine)
			throws IOException, CompileException {
		
		if (!lookedUpJava) {
			File f = new File("javaLookup.txt");
			// use the javadocs online to lookup the list and create it
			if (!f.exists()) {
				URL url = new URL("https://docs.oracle.com/javase/8/docs/api/allclasses-noframe.html");
				Scanner sc = new Scanner(url.openStream());
				PrintWriter pw = new PrintWriter("javaLookup.txt");
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					if (line.matches("<li><a href=\".*")) {
						String name = line.replaceAll("<li><a href=\"", "");
						name = name.replaceAll("\\.html.*", "");
						pw.println(name);
					}
				}
				sc.close();
				pw.flush();
				pw.close();
			}
			// read from the list
			List<String> lines = Files.readAllLines(f.toPath());
			for (String line : lines) {
				// just the last part of the fully qualified name
				String shortestName = line.replaceAll(".*/", "");
				javaItems.put(shortestName, line);
				syms.putEntry(line, "className", fileName, fileLine);
			}
			lookedUpJava = true;
		}
		// now we know javaItems has the reference
		return javaItems.get(shortName);
	}
}
