package tree;

import helper.BinaryOperation;
import helper.ClassLookup;
import helper.CompileException;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.StoreAddressStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** left type right */
public class AssignmentNode extends NodeImpl implements StatementExprNode, Expression {

	@NotNull private final Expression left;
	@Nullable private final BinaryOperation type; //null means = operation, other is the compound type.
	@NotNull private final Expression right;

    public AssignmentNode(@NotNull String fileName, int line, @NotNull Expression left,
						  @NotNull Expression right, @Nullable BinaryOperation type) {
    	super(fileName, line);
    	this.left = left;
    	this.right = right;
    	this.type = type;
    }
	
	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f)
			throws CompileException {
		
		if (type == null) {
			if (!(left instanceof LValue)) {
				throw new CompileException("left side of = expression not able to assign to. " 
							+ left.toString(), getFileName(), getLine());
			}
			right.compile(s, f);
			Register rightResult = f.allocator.getLast();
			
			LValue leftSide = (LValue)left;
			leftSide.compileAddress(s, f);
			Register leftAddress = f.allocator.getLast(); // a reference
			
			// store the result of the right side into the address
			StoreAddressStatement store = new StoreAddressStatement(rightResult, leftAddress,
					getFileName(), getLine());
			
			f.addStatement(store);
			
			// the result of an assign is the expression on the right
			// this allows for x = y = 5;
			Register result = f.allocator.getNext(rightResult.getType());
			f.addStatement(new CopyStatement(rightResult, result, getFileName(), getLine()));
		} else {
			// dissolve the compound assignment down to left = left OPERATION right
			BinaryExpressionNode rightSide = new BinaryExpressionNode(getFileName(), getLine(), left, right, type);
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine(), left, rightSide, type);
			assign.compile(s,f);
		}
	}
	
}
