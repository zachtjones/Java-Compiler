
public class OutOfRegisters {

	public static void main(String[] args) {
		long a = 1L;
		long b = 2L;
		long c = 3L;
		long d = 4L;
		long e = 5L;
		long f = 6L;
		long g = 7L;
		long h = 8L;
		long i = 9L;
		long j = 10L;
		long k = 11L;
		long l = 12L;
		long m = 13L;

		long z = a + b + c + d + e + f + g + h + i + j + k + l + m;

		System.out.print(z); // should be 91
	}
}