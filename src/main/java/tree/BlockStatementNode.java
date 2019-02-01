package tree;

/**
 * This interface represents a statement in a block, which can either be a StatementNode or a LocalVariable.
 * This is used primarily in the parser to help with type checking.
 */
public interface BlockStatementNode extends Node {}
