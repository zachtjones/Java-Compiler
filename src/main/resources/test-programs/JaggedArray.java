import java.util.Arrays;

public class JaggedArray {

	public static void main(String[] args) {
		// build a triangle of numbers
		short[][] c = new short[4][];

		for (int i = 0; i < c.length; i++) {
			c[i] = new short[i + 1];
			// filled in with 0's
		}

		// print out the arrays
		for (int i = 0; i < c.length; i++) {
			System.out.println(Arrays.toString(c[i]));
		}
	}
}