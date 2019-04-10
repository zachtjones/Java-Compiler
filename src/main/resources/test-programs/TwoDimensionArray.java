import java.util.Arrays;

public class TwoDimensionArray {

	public static void main(String[] args) {
		// place an X on the main diagonal, O everywhere else.
		char[][] c = new char[3][3];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j <= c[i].length; j++) {
				if (i == j) {
					c[i][j] = 'X';
				} else {
					c[i][j] = 'O';
				}
			}
		}

		// print out the arrays
		for (int i = 0; i < c.length; i++) {
			System.out.println(Arrays.toString(c[i]));
		}
	}
}