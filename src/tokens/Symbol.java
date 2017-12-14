package tokens;

public class Symbol implements Token {
	public String contents;
	
	public Symbol(String contents) {
		this.contents = contents;
	}
	
	public String toString() {
		return "Symbol: " + contents;
	}
}
