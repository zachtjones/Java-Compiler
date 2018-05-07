package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class NameNode implements Node {
    public String primaryName;
    public NameNode extendsNode; // used in class / interface declarations
    public ArrayList<NameNode> generics;
    public NameNode secondaryName;
    
	/** Gets the full name for this name node, using the secondary name.
	 * Generics are not filled in */
	public String getFullName() {
		StringBuilder result = new StringBuilder(primaryName);
		if (secondaryName != null) {
			result.append('.');
			result.append(secondaryName.getFullName());
		}
		return result.toString();
	}

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		String thisFull = getFullName();
		System.out.println("Old name: " + thisFull);
		String lookup = c.getFullName(thisFull);
		// replace with looked up value
		this.primaryName = lookup;
		this.secondaryName = null;
		
		System.out.println("\tNew name: " + lookup);
		// TODO handle generics and the ? extends / super thing
	}
    
}
