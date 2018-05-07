package tree;

import java.io.IOException;
import java.util.ArrayList;

import helper.ClassLookup;

public class ExpressionNode implements Node {
    // any of the int constants are from JavaParserConstants class.

    // these are set based on the types used below
    public ExpressionNode op1;
    public ExpressionNode op2;
    public ExpressionNode op3;

    // this is used for where there's a chain of expressions
    //  (ex: a || b || c || d)
    public ArrayList<ExpressionNode> ops;
    // this is used for where there's options for different tokens
    //   between the expressions
    public ArrayList<Integer> tokens;

    // used in instanceof expression
    public TypeNode type1;

    // set if this is an assignment (op1 assignType op2)
    //  this is =, +=, ... >>>=
    public int assignType;

    // op1 ? op2 : op3
    public boolean isConditionalTernary;

    // chain of logical or expressions -- see ops field
    public boolean isConditionalOr;

    // chain of logical and expressions -- see ops field
    public boolean isConditionalAnd;

    // chain of bitwise or expressions -- see ops field
    public boolean isBitwiseOr;

    // chain of bitwise xor expressions -- see ops field
    public boolean isBitwiseXOr;

    // chain of bitwise and expressions -- see ops field
    public boolean isBitwiseAnd;

    // chain of == / != expressions -- see ops field
    //   see the tokens also
    public boolean isEqualsCondition;

    // op1 instanceof type1
    public boolean isInstanceOfExpression;

    // chain of <, >, <=, >= expressions -- see ops field
    //   see the tokens also
    public boolean isRelationalExpression;

    // chain of <<, >>, >>> expressions -- see ops field
    //    see the tokens also
    public boolean isShiftExpression;

    // chain of + or - expressions -- see ops field
    //    see the tokens also
    public boolean isAdditiveExpression;

    // chain of *, /, or % expressions -- see ops field
    //    see the tokens also
    public boolean isMultiplicativeExpression;

    // true if this is a unary - expression
    // -op1
    public boolean isUnaryMinusExpression;

    // ++ op1
    public boolean isPreIncrementExpression;

    // -- op1
    public boolean isPreDecrementExpression;

    // ~ op1
    public boolean isBitwiseNotExpression;

    // ! op1
    public boolean isLogicalNotExpression;

    // op1 ++
    public boolean isPostIncrementExpression;

    // op1 --
    public boolean isPostDecrementExpression;

    // (PrimitiveType arrayDims) op1
    public boolean isPrimitiveCast;
    public PrimitiveTypeNode primitiveType;

    public int arrayDims;

    // (ObjectType arrayDims) op1
    public boolean isObjectCast;
    public NameNode objectType;

    // primaryPrefix (primarySuffix)*
    //  ops holds the suffixes, op1 is the prefix
    public boolean isPrimarySuffix;

    // set if this expression is a literal value
    public String literalValue;

    // set if this expression is a name
    public NameNode name;

    // true if this expression is just "this"
    public boolean isThis;

    // the identifier of    super.IDENTIFIER
    public String superIdentifier;

    // [op1]
    public boolean isArrayIndexExpression;

    // .propertyIdentifier   (could be part of function call)
    public String propertyIdentifier;

    // (ops...)  -- ops can be empty list for ()
    public boolean isArguments;

    // primitive array allocation -- see primitiveType
    //   also see ops for the array init expressions
    //    -- ops has nulls for empty expressions
    //   ex new boolean[4][x][] is valid
    public boolean isArrayAllocPrim;

    // new ClassName[]...
    //   see name for the class name
    //   ops for the array inits
    //     -- ops has nulls for empty expressions
    public boolean isArrayAllocClass;

    // new ClassName(args...)
    //    see name for the class name, op1 for args
    public boolean isConstructorCall;

    
	@Override
	public void resolveNames(ClassLookup c) throws IOException {
		// for simplicity, check all fields
		if (op1 != null) op1.resolveNames(c);
		if (op2 != null) op2.resolveNames(c);
		if (op3 != null) op3.resolveNames(c);
		if (ops != null) {
			for (ExpressionNode e : ops) {
				e.resolveNames(c);
			}
		}
		if (type1 != null) {
			type1.resolveNames(c);
		}
		if (objectType != null) {
			objectType.resolveNames(c);
		}
		if (name != null) {
			name.resolveNames(c);
		}
	}

}
