package tree;

import java.util.ArrayList;

public class ForInitNode implements Node {
    // either these first two, or the second
    public LocalVariableDecNode dec;
    // or this:
    public ArrayList<StatementExprNode> items;
}
