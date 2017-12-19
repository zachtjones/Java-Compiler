package tokens;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import declarations.ArgType;
/**
 * Represents a class, interface, or enum's name in the token sequence.
 */
public class ClassToken implements Token, ArgType {
	
	public String shortName; // (ex: Random)
	public String longName; //fully qualified (ex: java.util.Random)
	
	private static HashMap<String, String> names = new HashMap<String, String>(); // shortName to longName
	
	public ClassToken(String name) {
		String[] temp = name.split("\\.");
		this.shortName = temp[temp.length - 1]; // grab the last part -- this works for fully qualified and not
		this.longName = names.get(shortName);
	}
	@Override
	public String toString() {
		return "Class: '" + this.longName + "'";
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof ClassToken && ((ClassToken)o).longName == this.longName;
	}
	
	/**
	 * Returns true if this token is a Class name, having setup this class with the java library directory and
	 * the imports to the file.
	 * @param token The string read in to see if it is a class's name
	 */
	public static boolean matches(String token) {
		return names.containsKey(token);
	}
	
	public static void setup(String libDirectory, HashSet<String> imports, String currFile, String packageId) {
		// add shortName -> longName and also longName -> longName for convenience
		// automatically import the classes in the current package and in java.lang.*
		imports.add("java.lang.*");
		imports.add(packageId + ".*");
		
		for (String i : imports) {
			if (i.endsWith("*")) {
				String tempDir = libDirectory;
				File libTemp = new File(libDirectory);
				if (!i.startsWith("java.")) { // look for relative paths
					libTemp = new File(currFile).getParentFile(); // gets the directory containing currFile
					// go up number of . in packageId + 1 directories to get to src folder
					int len = packageId.split("\\.").length;
					for (int j = 0; j < len; j++) {
						libTemp = libTemp.getParentFile(); // go up one
					}
					tempDir = libTemp.getAbsolutePath();
				}
				//get the files in ex: "java.util.*"
				String tempFile = tempDir + '/' + i.replace('.', '/');
				tempFile = tempFile.replace("*", "");
				for (File f : new File(tempFile).listFiles()) {
					if (f.isDirectory()) {
						continue;
					}
					if (!f.getName().endsWith(".java")) { // get only java files
						continue;
					}
					String fully = f.getAbsolutePath().replace(libTemp.getAbsolutePath(), "").replace(".java", "");
					fully = fully.replace('/', '.');
					if (fully.startsWith(".")) { fully = fully.substring(1); } // skip starting dot
					String shortened = f.getName().replace(".java", "");
					names.put(shortened, fully); // shortName -> longName
					names.put(fully, fully); // longName -> longName
				}
			} else {
				// i is like java.util.Random or tokens.ClassToken
				String[] temp = i.split("\\."); // regex -- need literal dot
				String shortened = temp[temp.length - 1];
				names.put(shortened, i); // shortName -> longName
				names.put(i, i); // longName -> longName
			}
			
		} // for loop of imports
	}
}
