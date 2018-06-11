package helper;

public class ArrayDimensions {
	/** Returns num times of "[]" concatenated*/
	public static String get(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append("[]");
		}
		return sb.toString();
	}
}
