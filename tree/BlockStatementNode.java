package tree;

import java.util.ArrayList;

public class BlockStatementNode implements Node {
    // only one of these is not null
    public StatementNode statement;
    public LocalVariableDecNode dec;
}
