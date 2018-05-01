package tree;

public class SwitchLabelNode implements Node {
    public ExpressionNode expression;
    public boolean isDefault; // if default, no expression
}
