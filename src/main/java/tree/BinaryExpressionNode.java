package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;

public class BinaryExpressionNode extends NodeImpl implements Expression {
	private final Expression left;
	private final Expression right;
	private final BinaryOperation op;

	public BinaryExpressionNode(String fileName, int line, Expression left, Expression right, BinaryOperation op) {
		super(fileName, line);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f) throws CompileException {
		// evaluate left
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		// evaluate right
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();

		// perform the binary operation on the 2
		Register destination = f.allocator.getNext(
			Register.getLarger(leftResult.getType(), rightResult.getType()));

		f.statements.add(new BinaryOpStatement(
			leftResult, rightResult,
			destination, op.getRepresentation(),
			getFileName(), getLine()));
	}
}
