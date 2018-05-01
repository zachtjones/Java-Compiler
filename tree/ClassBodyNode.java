package tree;

public class ClassBodyNode implements Node {
    // one of these will be not null
    public BlockNode staticInit;
    public ConstructorNode constructor;
    public MethodNode method;
    public FieldDeclarationNode field;
}
