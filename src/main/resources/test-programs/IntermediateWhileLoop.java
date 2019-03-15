
// Next level for loop, contains break and continue statements
public class IntermediateWhileLoop {

    public static void main(String[] args) {
        // should print 12346789 -- note the 5 is missing.
        long i = 1L;
        while (i < 100L) {
            if (i == 5L) {
                i++;
                continue;
            }
            if (i == 10L) {
                break;
            }
            System.out.print(i);
            i++;
        }
        System.out.println();
    }
}