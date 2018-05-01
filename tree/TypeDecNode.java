package tree;

public class TypeDecNode implements Node {
    // only one of these will not be null
    // if they are all null, then the code was just ';'
    public ClassNode c;
    public InterfaceNode i;
    public EnumNode e;
}
