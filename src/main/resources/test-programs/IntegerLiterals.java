
public class IntegerLiterals {

	public static void main(String[] args) {
		System.out.println(0x10); // 16
		System.out.println(020); // 16
		System.out.println(16);
		System.out.println(16L);
		System.out.println(0b10000);

		// also with underscores
		System.out.println(0x1__0); // 16
		System.out.println(0_2_0); // 16
		System.out.println(1___6);
		System.out.println(1_6L);
		System.out.println(0b1__0000);
	}
}