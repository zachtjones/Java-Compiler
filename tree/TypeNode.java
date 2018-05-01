package tree;

public class TypeNode implements Node {
    // one of these two will not be null
    public PrimitiveTypeNode primitive;
    public NameNode name;

    public int arrayDims; // 0 for not array;
}
