package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;

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
	public void resolveImports(ClassLookup c) throws IOException {
		// normal statement most likely
		if (statement != null) {
			statement.resolveImports(c);
		} else if (labeled != null) {
			labeled.resolveImports(c);
		} else if (block != null) {
			block.resolveImports(c);
		} else if (switchNode != null) {
			switchNode.resolveImports(c);
		} else if (ifNode != null) {
			ifNode.resolveImports(c);
		} else if (whileNode != null) {
			whileNode.resolveImports(c);
		} else if (doNode != null) {
			doNode.resolveImports(c);
		} else if (forNode != null) {
			forNode.resolveImports(c);
		} // don't need to do anything for break or continue;
		else if (returnNode != null) {
			returnNode.resolveImports(c);
		} else if (throwNode != null) {
			throwNode.resolveImports(c);
		} else if (synchNode != null) {
			synchNode.resolveImports(c);
		} else if (tryNode != null) {
			tryNode.resolveImports(c);
		}
	}

	@Override
	public void resolveSymbols(SymbolTable s) throws CompileException {
		if (statement != null) {
			statement.resolveSymbols(s);
		} else if (labeled != null) {
			labeled.resolveSymbols(s);
		} else if (block != null) {
			block.resolveSymbols(s);
		} else if (switchNode != null) {
			switchNode.resolveSymbols(s);
		} else if (ifNode != null) {
			ifNode.resolveSymbols(s);
		} else if (whileNode != null) {
			whileNode.resolveSymbols(s);
		} else if (doNode != null) {
			doNode.resolveSymbols(s);
		} else if (forNode != null) {
			forNode.resolveSymbols(s);
		} else if (breakNode != null) {
			breakNode.resolveSymbols(s);
		} else if (continueNode != null) {
			continueNode.resolveSymbols(s);
		} else if (returnNode != null) {
			returnNode.resolveSymbols(s);
		} else if (throwNode != null) {
			throwNode.resolveSymbols(s);
		} else if (synchNode != null) {
			synchNode.resolveSymbols(s);
		} else if (tryNode != null) {
			tryNode.resolveSymbols(s);
		}
	}



}
