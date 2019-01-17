package helper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javaLibrary.JavaLibraryLookup;
import tree.ImportNode;
import tree.NameNode;
import tree.SymbolTable;

public class ClassLookup {
	private HashMap<String, String> items = new HashMap<>();
	
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
			SymbolTable classLevel)	throws CompileException {

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
		imports.add(javaLang);

		for (ImportNode i : imports) {
			String name = i.name.primaryName;
			if (name.startsWith("java.")) {
				if (i.isAll) {
					String packageImported = i.name.primaryName;
					for (String className : JavaLibraryLookup.getClassesInPackage(packageImported)) {
						putNames(className.trim());
					}
				} else {
					putNames(name);
				}
			}
		}
		// put all the class names in the symbol table
		for (String i : items.values()) {
			// can't add it twice
			if (classLevel.lookupThisScope(i) == -1)
				classLevel.putEntry(i, Types.CLASS, fileName, -1);
		}
	}

	private void putNames(String name) {
		String simpleName = name.substring(name.lastIndexOf('.') + 1);
		String fullyQualified = name.replace('.', '/');
		items.put(simpleName, fullyQualified);
		items.put(fullyQualified, fullyQualified);
		items.put(name, fullyQualified);
	}

	/** Gets the full name from the short name representation.
	 * @param shortName the short name of this class/interface/enum
	 * */
	public String getFullName(String shortName) {

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
