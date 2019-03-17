
// Next level for loop, contains break and continue statements
public class IntermediateForLoop {

    public static void main(String[] args) {
        // should print 12346789 -- note the 5 is missing.
        for (long i = 1L; i < 100L; i++) {
            if (i == 5L) {
                continue;
            }
            if (i == 10L) {
                break;
            }
            System.out.print(i);
        }
        System.out.println();
    }
}