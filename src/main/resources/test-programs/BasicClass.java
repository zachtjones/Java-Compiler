// this file is for testing a static and instance field, as well as
//  minimal method example
public class BasicClass {
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
		//BasicClass temp = new BasicClass();
		//temp.setB(5L);
		BasicClass.setA(1L);

		//System.out.print("b is: ");
		//System.out.println(temp.getB());
		System.out.print("a is: ");
		System.out.println(BasicClass.getA());
	}
}
