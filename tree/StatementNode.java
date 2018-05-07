package tree;

import java.io.IOException;

import helper.ClassLookup;

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

	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// normal statement most likely
		if (statement != null) {
			statement.resolveNames(c);
		} else if (labeled != null) {
			labeled.resolveNames(c);
		} else if (block != null) {
			block.resolveNames(c);
		} else if (switchNode != null) {
			switchNode.resolveNames(c);
		} else if (ifNode != null) {
			ifNode.resolveNames(c);
		} else if (whileNode != null) {
			whileNode.resolveNames(c);
		} else if (doNode != null) {
			doNode.resolveNames(c);
		} else if (forNode != null) {
			forNode.resolveNames(c);
		} // don't need to do anything for break or continue;
		else if (returnNode != null) {
			returnNode.resolveNames(c);
		} else if (throwNode != null) {
			throwNode.resolveNames(c);
		} else if (synchNode != null) {
			synchNode.resolveNames(c);
		} else if (tryNode != null) {
			tryNode.resolveNames(c);
		}
	}



}
