
public class TwoVariableForLoop {

    public static void main(String[] args) {
        // should print 123456789
        // 2-1, 4-2, 6-3, 8-4, ...
        for (long i = 1L, j = 2L; i < 10L; i++, j += 2) {
            System.out.print(j - i);
        }
        System.out.println();
    }
}