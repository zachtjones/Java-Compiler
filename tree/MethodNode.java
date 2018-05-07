package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class MethodNode implements Node {
    public boolean isPublic;
    public boolean isProtected;
    public boolean isPrivate;
    public boolean isStatic;
    public boolean isAbstract;
    public boolean isFinal;
    public boolean isNative;
    public boolean isSynchronized;
    public ResultTypeNode resultType;

    public MethodDeclaratorNode dec;
    public ArrayList<NameNode> throwsList;
    public BlockNode code;
    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		resultType.resolveNames(c);
		dec.resolveNames(c);
		if (throwsList != null) {
			for (NameNode n : throwsList) {
				n.resolveNames(c);
			}
		}
		code.resolveNames(c);
	}

}
