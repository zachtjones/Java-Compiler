package tree;

/*** This interface is the same as Node, but represents a subset, the Statements (Try, If, While, Block, ...)
 * Again, this is mainly used in type checking in the parser */
public interface StatementNode extends BlockStatementNode {}
