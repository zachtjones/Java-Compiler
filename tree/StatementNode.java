package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.InterFunction;
import intermediate.RegisterAllocator;

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
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r) throws CompileException {
		if (statement != null) {
			statement.compile(s, f, r);
		} else if (labeled != null) {
			labeled.compile(s, f, r);
		} else if (block != null) {
			block.compile(s, f, r);
		} else if (switchNode != null) {
			switchNode.compile(s, f, r);
		} else if (ifNode != null) {
			ifNode.compile(s, f, r);
		} else if (whileNode != null) {
			whileNode.compile(s, f, r);
		} else if (doNode != null) {
			doNode.compile(s, f, r);
		} else if (forNode != null) {
			forNode.compile(s, f, r);
		} else if (breakNode != null) {
			breakNode.compile(s, f, r);
		} else if (continueNode != null) {
			continueNode.compile(s, f, r);
		} else if (returnNode != null) {
			returnNode.compile(s, f, r);
		} else if (throwNode != null) {
			throwNode.compile(s, f, r);
		} else if (synchNode != null) {
			synchNode.compile(s, f, r);
		} else if (tryNode != null) {
			tryNode.compile(s, f, r);
		}
	}



}
