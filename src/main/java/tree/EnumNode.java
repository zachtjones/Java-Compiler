package tree;

import helper.ClassLookup;
import helper.CompileException;
import helper.Types;
import intermediate.InterFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EnumNode implements TypeDecNode {
    @NotNull private final String fileName;
	private final int line;
	@NotNull private final String name;
	@NotNull private final ArrayList<String> values;

    public EnumNode(@NotNull String fileName, int line, @NotNull String name, @NotNull ArrayList<String> values) {

		this.fileName = fileName;
		this.line = line;
		this.name = name;
		this.values = values;
	}

	@Override
	public void resolveImports(@NotNull ClassLookup c) throws CompileException {
		// nothing needed
	}

	/**
	 * Generates an intermediate file for this enum node.
	 * @param packageName The package's name, or null for default package.
	 * @param classLevel The classLevel symbols (from imports)
	 * @return A new intermediate file.
	 */
	@Override
	public InterFile compile(@Nullable String packageName, @NotNull SymbolTable classLevel) {
		InterFile f;
		if (packageName != null) {
			f = new InterFile(packageName + "." + this.name, "java/lang/Enum", new ArrayList<>());
		} else {
			f = new InterFile(this.name, "java/lang/Enum", new ArrayList<>());
		}
		for (int i = 0; i < values.size(); i++) {
			String id = values.get(i);
			f.addField(Types.INT, id, true, String.valueOf(i));
		}
		return f;
	}

	
}
