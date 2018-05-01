package tree;

import java.util.ArrayList;

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

}
