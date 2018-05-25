package tree;

import java.util.ArrayList;

/** ( expressions * )
* This is the second part of a function call, the arguments list. */
public class ArgumentExpressionNode implements ExpressionNode {
    /** The expressions to evaluate before the function call. Could be empty, but will not be null. */
    public ArrayList<ExpressionNode> expressions = new ArrayList<ExpressionNode>();
}
