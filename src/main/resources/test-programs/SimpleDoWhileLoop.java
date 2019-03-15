
public class SimpleDoWhileLoop {

    public static void main(String[] args) {
        // should print 123456789
        long i = 1L;
        do {
            System.out.print(i);
            i++;
        } while (i < 10L);
        System.out.println();
    }
}