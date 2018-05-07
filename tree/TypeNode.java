package tree;

import java.io.IOException;

import helper.ClassLookup;

public class TypeNode implements Node {
    // one of these two will not be null
    public PrimitiveTypeNode primitive;
    public NameNode name;

    public int arrayDims; // 0 for not array;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		if (name != null) {
			name.resolveNames(c);
		}
	}
}
