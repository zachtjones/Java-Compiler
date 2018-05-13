package tree;

import java.io.IOException;

import helper.ClassLookup;
import intermediate.InterFile;

public class TypeDecNode implements Node {
    // only one of these will not be null
    // if they are all null, then the code was just ';'
    public ClassNode c;
    public ClassNode i; // interfaces are treated as abstract classes in this
    public EnumNode e;
    
	@Override
	public void resolveNames(ClassLookup c1) throws IOException {
		if (c != null) {
			c.resolveNames(c1);
		} else if (i != null) {
			i.resolveNames(c1);
		} else if (e != null) {
			e.resolveNames(c1);
		}
		
	}

	/**
	 * Passes down the compile job to the class/interface/enum
	 * @param packageName the package's name, or null if default.
	 * @return The intermediate file node.
	 */
	public InterFile compile(String packageName) {
		if (this.c != null) {
			return c.compile(packageName);
		} else if (this.i != null) {
			return i.compile(packageName);
		} else if (this.e != null){
			return e.compile(packageName);
		}
		// empty type declaration (just ; ) , or not implemented yet.
		return null;
	}
}
