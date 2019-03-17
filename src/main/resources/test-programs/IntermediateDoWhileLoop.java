
// Next level for loop, contains break and continue statements
public class IntermediateDoWhileLoop {

    public static void main(String[] args) {
        // should print 12346789 -- note the 5 is missing.
        long i = 1L;
        do {
            if (i == 5L) {
                i++;
                continue;
            }
            if (i == 10L) {
                break;
            }
            System.out.print(i);
            i++;
        } while (i < 100);
        System.out.println();
    }
}