import java.util.Scanner;

public class MorseVowel {

	static char[] contents;
	static int n;

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		//skip the first line, but grab the next
		sc.nextLine();
		String line = sc.nextLine();
		sc.close();
		// split the next line's contents into a char array for convenience.
		n = line.length();
		contents = new char[n];
		for (int i = 0; i < n; i++ ) {
			contents[i] = line.charAt(i);
		}

		System.out.println(combos());
	}

	/** Determines the amount of combinations of only vowels
	 * for the contents, and returns that number. */
	private static long combos() {
		// 2^32 is not large enough for all combinations up to length 100,
		//   so use a long as the array type.
		long[] results = new long[n + 1];

		// base case: 1 way to make an empty string
		results[0] = 1;

		// go backwards from the end of the string
		for (int i = 1; i <= n; i++) {
			// test e - go back one
			if (matches('e', n - i)) { results[i] += results[i - 1];	}
			// test a and i, go back 2
			if (i >= 2) {
				if (matches('a', n - i)) { results[i] += results[i - 2]; }
				if (matches('i', n - i)) { results[i] += results[i - 2]; }
			}
			// test o and u, go back 3
			if (i >= 3) {
				if (matches('o', n - i)) { results[i] += results[i - 3]; }
				if (matches('u', n - i)) { results[i] += results[i - 3]; }
			}
		}

		return results[contents.length];
	}
	/** Helper method to see if the contents starting at
	 * offset could represent that character. */
	private static boolean matches(char value, int offset) {
		if (value == 'a') {
			return contents[offset] == '.' && contents[offset + 1] == '-';
		} else if (value == 'e') {
			return contents[offset] == '.';
		} else if (value == 'i') {
			return contents[offset] == '.' && contents[offset + 1] == '.';
		} else if (value == 'o') {
			return contents[offset] == '-' && contents[offset + 1] == '-'
					&& contents[offset + 2] == '-';
		} else if (value == 'u') {
			return contents[offset] == '.' && contents[offset + 1] == '.'
					&& contents[offset + 2] == '-';
		}
		/*switch(value) {
		case 'a':
			return contents[offset] == '.' && contents[offset + 1] == '-';
		case 'e':
			return contents[offset] == '.';
		case 'i':
			return contents[offset] == '.' && contents[offset + 1] == '.';
		case 'o':
			return contents[offset] == '-' && contents[offset + 1] == '-'
					&& contents[offset + 2] == '-';
		case 'u':
			return contents[offset] == '.' && contents[offset + 1] == '.'
					&& contents[offset + 2] == '-';
		}*/
		throw new IllegalArgumentException("Char: " + value);
	}

}
