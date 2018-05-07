package tree;

import java.io.IOException;

import helper.ClassLookup;

public class TypeDecNode implements Node {
    // only one of these will not be null
    // if they are all null, then the code was just ';'
    public ClassNode c;
    public InterfaceNode i;
    public EnumNode e;
    
	@Override
	public void resolveNames(ClassLookup c1) throws IOException {
		if (this.c != null) {
			this.c.resolveNames(c1);
		} else if (i != null) {
			i.resolveNames(c1);
		} else {
			e.resolveNames(c1);
		}
		
	}
}
