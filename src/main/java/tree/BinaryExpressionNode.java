package tree;

import helper.BinaryOperation;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.BinaryOpStatement;
import intermediate.InterFunction;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

public class BinaryExpressionNode extends NodeImpl implements Expression {
	@NotNull private final Expression left;
	@NotNull private final Expression right;
	@NotNull private final BinaryOperation op;

	public BinaryExpressionNode(@NotNull String fileName, int line, @NotNull Expression left,
								@NotNull Expression right, @NotNull BinaryOperation op) {
		super(fileName, line);
		this.left = left;
		this.right = right;
		this.op = op;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {
		// evaluate left
		left.compile(s, f);
		Register leftResult = f.allocator.getLast();
		// evaluate right
		right.compile(s, f);
		Register rightResult = f.allocator.getLast();

		// perform the binary operation on the 2
		Register destination = f.allocator.getNext(
			leftResult.getType().getResult(rightResult.getType()));

		f.addStatement(new BinaryOpStatement(
			leftResult, rightResult,
			destination, op,
			getFileName(), getLine()));
	}
}
