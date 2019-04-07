
/** This one tests getting the length property of an array. */
public class ArrayLength {

	static int[] createArray() {
		// generates out the fibonacci numbers
		int[] array = new int[10];
		array[0] = 1;
		array[1] = 1;

		// fill in the rest of the numbers
		for (int i = 2; i < 10; i++) {
			array[i] = array[i -1] + array[i - 2];
		}
		return array;
	}

	public static void main(String[] args) {
		// prints out the sum of:
		// [1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
		int[] a = createArray();
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		// the sum is 143
		System.out.println(sum);
	}
}