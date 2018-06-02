// this file is for testing a static and instance field, as well as
//  minimal method example
public class BasicClassQualified {
	public static int a = 1;
	public int b = 2;

	public void setB(int c) {
		this.b = c;
	}

	public int getB() {
		return this.b;
	}

	public static void setA(int c) {
		BasicClassQualified.a = c;
	}

	public static int getA() {
		return BasicClassQualified.a;
	}
}
