// this file is for testing a static and instance field, as well as
//  minimal method example
public class BasicClass2 {
	public static long a;
	public long b;

	public void setB(long c) {
		b = c;
	}

	public long getB() {
		return b;
	}

	public static void setA(long c) {
		a = c;
	}

	public static long getA() {
		return a;
	}


	public static void main(String[] args) {
		// expected: 12345

		BasicClass2 temp = new BasicClass2();
		temp.setB(1L);
		System.out.print(temp.getB());

		BasicClass2.setA(2L);
		System.out.print(BasicClass2.getA());

		// do the assignments directly
		temp.b = 3L;
		System.out.print(temp.b);

		BasicClass2.a = 4L;
		System.out.print(BasicClass2.a);

		// check assignment to the static variable without qualification
		a = 5L;
		System.out.println(a);
	}
}
