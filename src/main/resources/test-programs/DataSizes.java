
/** This one performs operations on various data sizes */
public class DataSizes {

	public static void main(String[] args) {
		boolean b = true;
		System.out.println(b);
		b = false;
		System.out.println(b);

		char a = 'a';
		System.out.print(a);
		a = (char) (a + 1);
		System.out.println(a);

		short s = (short) 23423;
		short s2 = (short) (s + s); // should overflow
		System.out.println(s2);

		int i = Integer.MAX_VALUE;
		System.out.println(i + 1); // should be Integer.MIN_VALUE

		long l = Integer.MAX_VALUE;
		System.out.println(l + 1L); // should just be 2^32.

	}

}