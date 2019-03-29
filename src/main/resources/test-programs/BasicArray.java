
import java.util.Arrays;

/** This class deals with storing the values in an array, and then uses a java class to print them out. */
public class BasicArray {

	public static void main(String[] args) {

		// prints [1, 2, 3, 4]

		int[] items = new int[4];
		items[0] = 1;
		items[1] = 2;
		items[2] = 3;
		items[3] = 4;

		System.out.println(Arrays.toString(items));
	}
}