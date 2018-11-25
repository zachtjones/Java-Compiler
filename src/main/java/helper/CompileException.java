package helper;

public class CompileException extends Exception {
	
	private String file;
	private int line;
	
	/** Constructs a new exception with the specified detail message */
	public CompileException(String message, String file, int line) {
		super(message);
		this.file = file;
		this.line = line;
	}

	/** Constructs a new exception with the specified detail message and cause. */
	public CompileException(String string, Throwable cause, String file, int line) {
		super(string, cause);
		this.file = file;
		this.line = line;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " @ " + file + ":" + line;
	}
}
