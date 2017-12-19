package declarations.statements;

import java.util.ArrayList;

import declarations.Statement;

public class IfStatement implements Statement {
	public Expression expression;
	public ArrayList<Statement> block = new ArrayList<Statement>();
}
