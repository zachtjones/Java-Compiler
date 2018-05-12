package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;
import intermediate.InterFile;

public class EnumNode implements Node {
    public String name;
    public ArrayList<String> values;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// nothing needed
	}

	/**
	 * Generates an intermediate file for this enum node.
	 * @param packageName The package's name, or null for default package.
	 * @return A new intermediate file.
	 */
	public InterFile compile(String packageName) {
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "." + name);
		} else {
			f = new InterFile(name);
		}
		for (int i = 0; i < values.size(); i++) {
			String id = values.get(i);
			f.addField("int", id, true, String.valueOf(i));
		}
		return f;
	}
}
