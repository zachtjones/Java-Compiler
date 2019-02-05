package helper;

import org.jetbrains.annotations.NotNull;

public class CompileException extends Exception {
	
	@NotNull private String file;
	private int line;
	
	/** Constructs a new exception with the specified detail message */
	public CompileException(@NotNull String message, @NotNull String file, int line) {
		super(message);
		this.file = file;
		this.line = line;
	}

	/** Constructs a new exception with the specified detail message and cause. */
	public CompileException(@NotNull String string, @NotNull Throwable cause, @NotNull String file, int line) {
		super(string, cause);
		this.file = file;
		this.line = line;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " @ " + file + ":" + line;
	}
}
