package tree;

import java.io.IOException;

import helper.ClassLookup;
import helper.CompileException;
import intermediate.CopyStatement;
import intermediate.InterFunction;
import intermediate.Register;
import intermediate.RegisterAllocator;
import intermediate.StoreAddressStatement;

/** left type right */
public class AssignmentNode implements Expression {
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
	
	@Override
	public void resolveImports(ClassLookup c) throws IOException {
		left.resolveImports(c);
		right.resolveImports(c);
	}

	@Override
	public void compile(SymbolTable s, InterFunction f, RegisterAllocator r, CompileHistory c) throws CompileException {
		if (type == ASSIGN) {
			if (!(left instanceof LValue)) {
				throw new CompileException("left side of = expression not able to assign to. " + left.toString());
			}
			right.compile(s, f, r, c);
			Register rightResult = r.getLast();
			
			LValue leftSide = (LValue)left;
			leftSide.compileAddress(s, f, r, c);
			Register leftAddress = r.getLast(); // a reference
			
			// store the result of the right side into the address
			StoreAddressStatement store = new StoreAddressStatement(rightResult, leftAddress);
			f.statements.add(store);
			
			// the result of an assign is the expression on the right
			// this allows for x = y = 5;
			Register result = r.getNext(rightResult.type);
			f.statements.add(new CopyStatement(rightResult, result));
		} else if (type == STARASSIGN) {
			// x *= 5 ->  x = x * 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			TimesExpressionNode mult = new TimesExpressionNode();
			mult.left = left;
			mult.right = right;
			assign.right = mult;
			
			// compile the new node created
			assign.compile(s, f, r, c);

		} else if (type == SLASHASSIGN) {
			// x /= 5 ->  x = x / 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			DivideExpressionNode div = new DivideExpressionNode();
			div.left = left;
			div.right = right;
			assign.right = div;

			// compile the new node created
			assign.compile(s, f, r, c);

		} else if (type == REMASSIGN) {
			// x %= 5 ->  x = x % 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			ModExpressionNode mod = new ModExpressionNode();
			mod.left = left;
			mod.right = right;
			assign.right = mod;

			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else if (type == PLUSASSIGN) {
			// x += 5 ->  x = x + 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			AddExpressionNode add = new AddExpressionNode();
			add.left = left;
			add.right = right;
			assign.right = add;
			
			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else if (type == MINUSASSIGN) {
			// x -= 5 ->  x = x - 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			SubtractExpressionNode sub = new SubtractExpressionNode();
			sub.left = left;
			sub.right = right;
			assign.right = sub;
			
			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else if (type == LSHIFTASSIGN) {
			// x -= 5 ->  x = x << 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			LeftShiftExpressionNode lefts = new LeftShiftExpressionNode();
			lefts.left = left;
			lefts.right = right;
			assign.right = lefts;

			// compile the new node created
			assign.compile(s, f, r, c);

		} else if (type == RSIGNEDSHIFTASSIGN) {
			// x -= 5 ->  x = x >> 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			RightShiftArithExpressionNode rights = new RightShiftArithExpressionNode();
			rights.left = left;
			rights.right = right;
			assign.right = rights;

			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else if (type == RUNSIGNEDSHIFTASSIGN) {
			// x -= 5 ->  x = x >> 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			RightShiftLogExpressionNode rights = new RightShiftLogExpressionNode();
			rights.left = left;
			rights.right = right;
			assign.right = rights;

			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else if (type == ANDASSIGN) {
			// x -= 5 ->  x = x >> 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			AndExpressionNode and = new AndExpressionNode();
			and.expressions.add(left);
			and.expressions.add(right);
			assign.right = and;

			// compile the new node created
			assign.compile(s, f, r, c);

		} else if (type == ORASSIGN) {
			// x -= 5 ->  x = x >> 5;
			AssignmentNode assign = new AssignmentNode();
			assign.left = left;
			AndExpressionNode and = new AndExpressionNode();
			and.expressions.add(left);
			and.expressions.add(right);
			assign.right = and;

			// compile the new node created
			assign.compile(s, f, r, c);
			
		} else {
			// unknown type of assignment.
			throw new CompileException("unknown type of Assignment: " + type);
		}
		
	}
	
}
