package helper;

public class CompileException extends Exception {
	
	/** Constructs a new exception with the specified detail message */
	public CompileException(String message) {
		super(message);
	}

	/** Constructs a new exception with the specified detail message and cause. */
	public CompileException(String string, Throwable cause) {
		super(string, cause);
	}
}
