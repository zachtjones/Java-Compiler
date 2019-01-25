package x64.directives;

public class SpaceDirective implements Directive {
	private int size;

	public SpaceDirective(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return ".space " + size;
	}
}
