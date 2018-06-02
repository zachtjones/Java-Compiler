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
			leftSide.compileAddress(s, f, r);
			Register leftAddress = r.getLast(); // a reference
			
			// store the result of the right side into the address
			StoreAddressStatement store = new StoreAddressStatement(rightResult, leftAddress);
			f.statements.add(store);
			
			// the result of an assign is the expression on the right
			// this allows for x = y = 5;
			Register result = r.getNext(rightResult.type);
			f.statements.add(new CopyStatement(rightResult, result));
		} else {
			// TODO - make a new tree node
			// x += 5 ->  x = x + 5;
			throw new CompileException("compound assignment not supported yet.");
		}
		
	}
	
}
