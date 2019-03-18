
public class FloatingPoint {

	public static void main(String[] args) {
		float f = 3.5f;
		System.out.println(f * 2.0f); // should be 7.0

		double g = 0.1;
		System.out.println(g / 0.0); // +infinity

		// from the javase specification
		int big = 1234567890;
		float approx = big;
		System.out.println(big - (int)approx); // should be -46
	}
}