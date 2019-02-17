package x64.directives;

public class SpaceDirective extends Directive {
	private int size;

	public SpaceDirective(int size) {
		this.size = size;
	}

	@Override
	public String assemblyRepresentation() {
		return ".space " + size;
	}
}
