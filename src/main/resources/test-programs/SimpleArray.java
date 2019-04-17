import java.util.Arrays;

public class SimpleArray {

	public static void main(String[] args) {
		// print out the fibonacci numbers
		int[] array = new int[10];
		array[0] = 1;
		array[1] = 1;

		// fill in the rest of the numbers
		for (int i = 2; i < 10; i++) {
			array[i] = array[i -1] + array[i - 2];
		}
		// print out results
		System.out.println(Arrays.toString(array));
	}
}