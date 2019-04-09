package tree;

import helper.*;
import intermediate.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static helper.BinaryOperation.ADD;

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

		// the whole way through this method, the last allocated register will be treated as the result.

    	// error case -- initial expressions array is empty without any other indexes.
		if (expressions.get(0) == null) {
			throw new CompileException(
				"The first dimension of an array has to have a size given at the constructor.",
				getFileName(), getLine());
		}

		// type is the overall array type
		// compile in all the expressions

		// list of registers that hold the sizes
		ArrayList<Register> sizes = new ArrayList<>();

		// work the way down the list, obtaining the sizes; also the elementType of the result
		Types elementType = type;
		while (!expressions.isEmpty()) {
			Expression first = expressions.remove(0);
			if (first == null) {
				// verify the rest are also null, handling case 4 described above
				for (int i = 1; i < expressions.size(); i++) {
					if (expressions.get(i) != null) {
						throw new CompileException("Invalid array creation", getFileName(), getLine());
					}
				}

				// if the rest are null, we can stop with the going down the list
				break;
			}
			first.compile(s, f);
			sizes.add(f.allocator.getLast());
			elementType = elementType.removeArray(getFileName(), getLine());
		}

		// first thing; the initial create array statement
		Register lastSize = sizes.remove(sizes.size() - 1);
		Register a = f.allocator.getNext(Types.UNKNOWN);

		ArrayList<InterStatement> allStatements = new ArrayList<>();
		allStatements.add(new CreateArrayStatement(lastSize, elementType, a, getFileName(), getLine()));

		// build up the all statements list, with the parts of the for loop before and after what currently
		//   has been built up so far
		while (!sizes.isEmpty()) {

			// create new array, call it a
			// for (int i = 0; i < lastSize; i++) {
			//    what already is there, creating the array elements -> inner
			//    put inner into a[i]
			// }

			ArrayList<InterStatement> thisIteration = new ArrayList<>();

			// get the new size
			lastSize = sizes.remove(sizes.size() - 1);
			Register inner = a;

			// create the new array, and call it a
			elementType = Types.arrayOf(elementType);
			a = f.allocator.getNext(Types.UNKNOWN);
			thisIteration.add(new CreateArrayStatement(lastSize, elementType, a, getFileName(), getLine()));

			// int i = 0;
			final String iName = ".i" + f.allocator.getNextLabel();
			// declare int i;
			thisIteration.add(new StartScopeStatement(iName, Types.INT));
			// get 0 into a register
			thisIteration.add(new LoadLiteralStatement("0", f.allocator, getFileName(), getLine()));
			// store that register into the local
			thisIteration.add(new PutLocalStatement(f.allocator.getLast(), iName, getFileName(), getLine()));

			// if we decompose that for loop to the intermediate code: (already have the init part done)
			//   init code
			// conditionLabel:
			//   boolean b = i < lastSize;
			//   branch when b is false to end
			//
			//   the current list of statements from the previous iteration
			//   put that into a[i]
			//   // update step
			//   putLocal i = i + 1;
			//   jump to conditionLabel
			// end:

			LabelStatement conditionLabel = new LabelStatement("condition" + f.allocator.getNextLabel());
			LabelStatement endLabel = new LabelStatement("end" + f.allocator.getNextLabel());

			thisIteration.add(conditionLabel);

			// boolean b = i < lastSize
			Register b = f.allocator.getNext(Types.BOOLEAN);
			Register iCondition = f.allocator.getNext(Types.INT);
			// get local iName -> iCondition
			thisIteration.add(new GetLocalStatement(iCondition, iName, getFileName(), getLine()));
			thisIteration.add(
				new SetConditionStatement(ConditionCode.LESS, iCondition, lastSize, b, getFileName(), getLine())
			);

			// branch when b is false to end
			thisIteration.add(new BranchStatementFalse(endLabel, b, getFileName(), getLine()));

			// the current list of statements from the previous iteration
			thisIteration.addAll(allStatements);

			// inner holds the pointer to the inner elements just created
			// put inner into a[i]
			Register iIndex = f.allocator.getNext(Types.INT);
			thisIteration.add(new GetLocalStatement(iIndex, iName, getFileName(), getLine()));
			Register pointer = f.allocator.getNext(Types.UNKNOWN); // filled in on type checking anyways
			thisIteration.add(new GetArrayValueAddressStatement(a, iIndex, pointer, getFileName(), getLine()));
			thisIteration.add(new StoreAddressStatement(inner, pointer, getFileName(), getLine()));

			// update i = 1 + 1:
			//   - load literal i -> one
			//   - getLocal i -> updateI
			//   - updatedI = updateI + one
			//   - putLocal i = updatedI
			thisIteration.add(new LoadLiteralStatement("1", f.allocator, getFileName(), getLine()));
			Register one = f.allocator.getLast();

			Register updateI = f.allocator.getNext(Types.INT);
			thisIteration.add(new GetLocalStatement(updateI, iName, getFileName(), getLine()));

			Register updatedI = f.allocator.getNext(Types.INT);
			thisIteration.add(new BinaryOpStatement(updateI, one, updatedI, ADD, getFileName(), getLine()));

			thisIteration.add(new PutLocalStatement(updatedI, iName, getFileName(), getLine()));

			// jump to condition label
			thisIteration.add(new JumpStatement(conditionLabel));

			// insert the ending label
			thisIteration.add(endLabel);

			// prepare for next step
			allStatements = thisIteration;
		}

		// a holds the array, might not be the last register allocated, so will need to insert a copy
		Register last = f.allocator.getNext(Types.UNKNOWN);
		allStatements.add(new CopyStatement(a, last, getFileName(), getLine()));

		// add the rest of the statements to the list
		for (InterStatement st : allStatements) {
			f.addStatement(st);
		}

		// TODO also merge in the code here for the object arrays, the logic is basically the same
	}
}
