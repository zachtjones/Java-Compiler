package declarations.statements;

import java.util.ArrayList;

import declarations.Statement;

public class ForStatement implements Statement {
	public Statement init;
	public Expression condition;
	public Statement increment;
	public ArrayList<Statement> block = new ArrayList<Statement>();
}
