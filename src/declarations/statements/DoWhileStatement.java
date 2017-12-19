package declarations.statements;

import java.util.ArrayList;

import declarations.Statement;

public class DoWhileStatement implements Statement {
	public ArrayList<Statement> block = new ArrayList<Statement>();
	public Expression expression;
}
