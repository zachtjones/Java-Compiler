package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.CreateArrayStatement;
import intermediate.InterFunction;
import intermediate.PutLocalStatement;
import intermediate.Register;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/** new type[expression or empty] ... */
public class PrimitiveArrayAllocationNode extends NodeImpl implements Expression {
    public Types type;
    public ArrayList<Expression> expressions; // never empty list, but can have null in the list
    
    public PrimitiveArrayAllocationNode(String fileName, int line) {
    	super(fileName, line);
    }

    @Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
    	// recursively go down and build the type up.
		//   type before this call is the base element type, we need the overall array type
		for (Expression e : expressions) {
			if (e != null) {
				e.resolveImports(c);
			}
			type = Types.arrayOf(type);
		}
	}
    
	@Override
	public void compile(@NotNull SymbolTable s, @NotNull InterFunction f) throws CompileException {

    	// case1: int[] x = new int[]; // not okay
		// case2: int[][] x = new int[4][] // okay
		// case3: int[][] x = new int[4][7] // also okay
		// case4: int[][][] x = new int[4][][4] // not okay

    	// error base case -- initial expressions array is empty without any other indexes.
		if (expressions.get(0) == null) {
			throw new CompileException(
				"The first dimension of an array has to have a size given at the constructor.",
				getFileName(), getLine());
		}

		// not-null expression base case
		Types elementType = type.removeArray(getFileName(), getLine());
		if (expressions.size() == 1) {
			expressions.get(0).compile(s, f);
			Register size = f.allocator.getLast();
			Register result = f.allocator.getNext(Types.UNKNOWN);
			f.addStatement(new CreateArrayStatement(size, elementType, result, getFileName(), getLine()));
			return;
		}

		// not a base case, we have established that the first expression is defined (not null)
		Expression first = expressions.remove(0);
		first.compile(s, f);
		Register size = f.allocator.getLast();

		// the allocation of the element type
		Register inner = f.allocator.getNext(elementType);
		f.addStatement(new CreateArrayStatement(size, elementType, inner, getFileName(), getLine()));

		// two cases left -- one with the next element null, which means stop recurring
		//  the other case means to create a for loop with a new local var to create the sub-elements
		if (expressions.get(0) == null) {
			// verify the rest are also null, handling case 4 described above
			for (int i = 1; i < expressions.size(); i++) {
				if (expressions.get(i) != null) {
					throw new CompileException("Invalid array creation", getFileName(), getLine());
				}
			}
			// rest are also null, we already added the create array statement so we're done
			// inner is the last allocated, so that's going to hold the result.
			return;
		}

		// new int[4][5] means:
		//  create a an array of length 4, type of elements is int[], call it 'a'
		//  for (int i = 0; i < 4; i++) {
		//    a[i] = new int[5];
		// we need both a and i to be variables that couldn't possibly be locals in the .java file,
		//   so we can just give them names like .a13 or .i34

		// introduce a new symbol table
		SymbolTable newTable = new SymbolTable(s, SymbolTable.local);

		final String aName = ".a" + f.allocator.getNextLabel();
		final String iName = ".i" + f.allocator.getNextLabel();

		newTable.putEntry(aName, type, getFileName(), getLine());
		newTable.putEntry(iName, Types.INT, getFileName(), getLine());

		// do a set local inner -> .a
		f.addStatement(new PutLocalStatement(inner, aName, getFileName(), getLine()));

		// create the for loop to initialize the rest of the elements - the loop counter is iName
		// initializer: int iName = 0;
		VariableDecNode iDec = new VariableDecNode(getFileName(), getLine());
		iDec.id = new VariableIdNode(getFileName(), getLine());
		iDec.id.name = iName;
		iDec.init = new VariableInitializerNode(getFileName(), getLine());
		iDec.init.e = new LiteralExpressionNode(getFileName(), getLine(), "0");
		ArrayList<VariableDecNode> inits = new ArrayList<>();
		inits.add(iDec);
		ForInitNode init = new LocalVariableDecNode(getFileName(), getLine(), Types.INT, inits);

		// condition: iName < size
		// TODO

		// update: i++
		PostIncrementExpressionNode update = new PostIncrementExpressionNode(getFileName(), getLine());
		update.expr = new UnknownIdentifierNode(iName, getFileName(), getLine());

		// block is the recursive call -- might need a helper method for this
		// TODO

		// we have created the for loop
		ForStatementNode forStatementNode = new ForStatementNode(getFileName(),getLine(),
			init, condition, update, block);

		// compile the for loop
		forStatementNode.compile(newTable, f);


		// inner holds the array
		// in java, an 2+ dimension array is an array of arrays
		// for (int <var> = 0; <var> < expressions[0]; <var>++)
		//    create the array with one less dimension
		// only do the completely recursive call if the next expression isn't null

		newTable.endScope(f);

		// TODO handle multi-dimensional arrays.

		// TODO also merge in the code here for the object arrays, the logic is basically the same
		throw new CompileException("Multi-dimensional array creation not done yet.", "", -1);

	}
}
