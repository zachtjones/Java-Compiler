package tree;

import java.util.ArrayList;

/** Represents a CompilationUnit, that is a source file.
*  @author Zach Jones */
public class CompilationUnit implements Node {
    public NameNode packageName;
    public ArrayList<ImportNode> imports = new ArrayList<>();
    public ArrayList<TypeNode> types = new ArrayList<>();
}
