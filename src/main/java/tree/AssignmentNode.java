package tree;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.StoreAddressStatement;

import static tree.BinaryOperation.*;

/** left type right */
public class AssignmentNode extends NodeImpl implements StatementExprNode, Expression {
	public final static int ASSIGN = 0;
	public final static int STARASSIGN = 1;
	public final static int SLASHASSIGN = 2;
	public final static int REMASSIGN = 3;
	public final static int PLUSASSIGN = 4;
	public final static int MINUSASSIGN = 5;
	public final static int LSHIFTASSIGN = 6;
	public final static int RSIGNEDSHIFTASSIGN = 7; // >>=
	public final static int RUNSIGNEDSHIFTASSIGN = 8; // >>>=
	public final static int ANDASSIGN = 9;
	public final static int XORASSIGN = 10;
	public final static int ORASSIGN = 11;

	public Expression left;
	public int type;
	public Expression right;

    public AssignmentNode(String fileName, int line) {
    	super(fileName, line);
    }
	
	@Override
	public void resolveImports(ClassLookup c) throws CompileException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f)
			throws CompileException {
		
		if (type == ASSIGN) {
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
		} else if (type == STARASSIGN) {
			// x *= 5 ->  x = x * 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, TIMES);
			
			// compile the new node created
			assign.compile(s, f);

		} else if (type == SLASHASSIGN) {
			// x /= 5 ->  x = x / 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, DIVIDE);

			// compile the new node created
			assign.compile(s, f);

		} else if (type == REMASSIGN) {
			// x %= 5 ->  x = x % 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, MOD);

			// compile the new node created
			assign.compile(s, f);
			
		} else if (type == PLUSASSIGN) {
			// x += 5 ->  x = x + 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, ADD);
			
			// compile the new node created
			assign.compile(s, f);
			
		} else if (type == MINUSASSIGN) {
			// x -= 5 ->  x = x - 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, SUBTRACT);
			
			// compile the new node created
			assign.compile(s, f);
			
		} else if (type == LSHIFTASSIGN) {
			// x -= 5 ->  x = x << 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, LEFT_SHIFT);

			// compile the new node created
			assign.compile(s, f);

		} else if (type == RSIGNEDSHIFTASSIGN) {
			// x -= 5 ->  x = x >> 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, RIGHT_SHIFT_SIGN);

			// compile the new node created
			assign.compile(s, f);
			
		} else if (type == RUNSIGNEDSHIFTASSIGN) {
			// x -= 5 ->  x = x >>> 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, RIGHT_SHIFT_UNSIGNED);

			// compile the new node created
			assign.compile(s, f);
			
		} else if (type == ANDASSIGN) {
			// x &= 5 ->  x = x & 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, AND);

			// compile the new node created
			assign.compile(s, f);

		} else if (type == ORASSIGN) {
			// x |= 5 ->  x = x | 5;
			AssignmentNode assign = new AssignmentNode(getFileName(), getLine());
			assign.left = left;
			assign.right = new BinaryExpressionNode(getFileName(), getLine(), left, right, OR);

			// compile the new node created
			assign.compile(s, f);
			
		} else {
			// unknown type of assignment.
			throw new CompileException("unknown type of Assignment: " + type, getFileName(), getLine());
		}
		
	}
	
}
