package tree;

import java.io.IOException;

import helper.ClassLookup;

public class ClassBodyNode implements Node {
    // one of these will be not null
    public BlockNode staticInit;
    public ConstructorNode constructor;
    public MethodNode method;
    public FieldDeclarationNode field;

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// pass down
		if (staticInit != null) {
			staticInit.resolveNames(c);
		} else if (constructor != null) {
			constructor.resolveNames(c);
		} else if (method != null) {
			method.resolveNames(c);
		} else if (field != null) {
			field.resolveNames(c);
		}
	}
}
