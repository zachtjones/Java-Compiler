package tree;

public class StatementNode implements Node {
    // only one of these are not null / true
    public LabeledStatementNode labeled;
    public BlockNode block;
    public boolean isEmpty; // empty node
    public StatementExprNode statement; // normal statement
    public SwitchStatementNode switchNode;
    public IfStatementNode ifNode;
    public WhileStatementNode whileNode;
    public DoStatementNode doNode;
    public ForStatementNode forNode;
    public BreakStatementNode breakNode;
    public ContinueStatementNode continueNode;
    public ReturnStatementNode returnNode;
    public ThrowStatementNode throwNode;
    public SynchronizedStatementNode synchNode;
    public TryStatementNode tryNode;
}
