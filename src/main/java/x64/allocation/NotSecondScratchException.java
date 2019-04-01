package x64.allocation;

/**
 * Class used when a second scratch register is needed, but there isn't one.
 */
public class NotSecondScratchException extends Exception {
	public NotSecondScratchException() {}
}
