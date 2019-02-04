package tree;

import org.jetbrains.annotations.NotNull;

public class ImportNode {
    @NotNull public final NameNode name;
    public final boolean isAll; // java.util.* would be all
    
    public ImportNode(@NotNull NameNode name, boolean isAll) {
    	this.name = name;
    	this.isAll = isAll;
    }
}
