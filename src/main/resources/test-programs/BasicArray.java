
import java.util.Arrays;

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