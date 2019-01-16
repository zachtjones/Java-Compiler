package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.StoreAddressStatement;

/** left type right */
public class AssignmentNode extends NodeImpl implements StatementExprNode, Expression {

	public Expression left;
	public BinaryOperation type;
	public Expression right;

    public AssignmentNode(String fileName, int line, Expression left, Expression right, BinaryOperation type) {
    	super(fileName, line);
    	this.left = left;
    	this.right = right;
    	this.type = type;
    }
	
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f)
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
			
			f.statements.add(store);
			
			// the result of an assign is the expression on the right
			// this allows for x = y = 5;
			Register result = f.allocator.getNext(rightResult.type);
			f.statements.add(new CopyStatement(rightResult, result, getFileName(), getLine()));
		} else {
			// dissolve the compound assignment down to left = left OPERATION right
			BinaryExpressionNode rightSide = new BinaryExpressionNode(getFileName(), getLine(), left, right, type);
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine(), left, rightSide, type);
			assign.compile(s,f);
		}
	}
	
}
