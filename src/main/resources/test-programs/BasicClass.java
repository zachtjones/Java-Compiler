// this file is for testing a static and instance field, as well as
//  minimal method example
public class BasicClass {
	public static int a;
	public int b;

	public void setB(int c) {
		b = c;
	}

	public int getB() {
		return b;
	}

	public static void setA(int c) {
		a = c;
	}

	public static int getA() {
		return a;
	}


	public static void main(String[] args) {
		//BasicClass temp = new BasicClass();
		//temp.setB(5);
		BasicClass.setA(1);

		//System.out.println("b is: " + temp.getB());
		System.out.println("a is: " + BasicClass.getA());
	}
}
