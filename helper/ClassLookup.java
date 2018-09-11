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
	private HashMap<String, String> items = new HashMap<String, String>();

	private SymbolTable syms;
	
	/**
	 * Loads the imports to reference the proper items.
	 * @param fileName The filename of the file currently being parsed.
	 * @param packageName The package name of the file currently being parsed,
	 *  or null.
	 * @param imports The ArrayList of ImportNodes that are declared in the final.
	 * @param classLevel The class level symbol table (from imports)
	 * @throws CompileException If there is an issue resolving creating this object.
	 */
	public ClassLookup(String fileName, String packageName, ArrayList<ImportNode> imports,
			SymbolTable classLevel) throws CompileException {
		
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
		
		// add in the java files
		try {
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
						pw.println(name.replace('.', '/'));
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
				items.put(shortestName, line);
				items.put(line, line);
				items.put(line.replace('/', '.'), line);
				syms.putEntry(line, "className", fileName, -1);
			}
		} catch(IOException e) {
			throw new CompileException(e.getMessage(), fileName, -1);
		}

	}

	/** Gets the full name from the short name representation.
	 * @param shortName the short name of this class/interface/enum
	 * @throws IOException A network is used to lookup the java all 
	 * classes for the first time.
	 * If there is an issue with this lookup, this exception is thrown.
	 * @throws CompileException If there is an error compiling.
	 * */
	public String getFullName(String shortName, String fileName, int line)
			throws CompileException {
		
		// handle Object -> java/lang/Object, 
		//   java.lang.Object -> java/lang/Object, 
		//   java/lang/Object -> java/lang/Object
		if (items.containsKey(shortName)) {
			return items.get(shortName).replace('.', '/');
		}
		
		// handle System.in -> java/lang/System.in
		if (shortName.contains(".")) {
			String firstPart = shortName.substring(0, shortName.indexOf("."));
			String rest = shortName.substring(shortName.indexOf(".") + 1);
			if (items.containsKey(firstPart)) {
				return items.get(firstPart) + "." + rest;
			}
		}
		
		// return the short name, assume default package;
		return shortName;
	}
}
