package helper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import tree.ImportNode;
import tree.NameNode;

public class ClassLookup {
	private static boolean lookedUpJava = false;
	private static HashMap<String, String> items = new HashMap<String, String>();
	private static HashMap<String, String> javaItems = new HashMap<String, String>();

	/**
	 * Loads the imports to reference the proper items.
	 * @param fileName The filename of the file currently being parsed.
	 * @param packageName The package name of the file currently being parsed,
	 *  or null.
	 * @param imports The ArrayList of ImportNodes that are declared in the final.
	 */
	public ClassLookup(String fileName, String packageName, ArrayList<ImportNode> imports) {

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
		ImportNode javaLang = new ImportNode();
		javaLang.isAll = true;
		javaLang.name = new NameNode();
		javaLang.name.primaryName = "java.lang";

	}

	/** Gets the full name from the short name representation.
	 * @param shortName the short name of this class/interface/enum
	 * @throws IOException A network is used to lookup the java all 
	 * classes for the first time.
	 * If there is an issue with this lookup, this exception is thrown.
	 * */
	public String getFullName(String shortName) throws IOException {
		if (items.containsKey(shortName)) {
			return items.get(shortName);
		}
		// check the java.*** stuff for it
		String javaLookup = lookupJavaName(shortName);
		if (javaLookup != null) return javaLookup;
		
		// return the short name, assume default package;
		return shortName;
	}

	/** Looks up the java name. Note this might require a network connection,
	 * and the java name could be java.util.* or javax.* or similar classes.
	 * */
	private String lookupJavaName(String shortName) throws IOException {
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
						name = name.replaceAll("/", "\\."); // replace / with .
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
				String shortestName = line.replaceAll(".*\\.", "");
				javaItems.put(shortestName, line);
			}
			lookedUpJava = true;
		}
		// now we know javaItems has the reference
		return javaItems.get(shortName);
	}
}
