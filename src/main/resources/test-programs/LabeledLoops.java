
// this class does all the logic of nested loops, with the outer and inner labels.
//   note that the inner labels aren't really needed, but still want to verify they work.
//  each one of the loop functions should have the same output.
// Could test all 9 variations, but nested of each in itself should be sufficient.
public class TwoVariableForLoop {

    public static StringBuilder forLoop() {
        // should be printing:
        //  1, 0
        //  2, 0
        //  2, 1
        //  4, 0
        //  4, 1
        //  4, 2
        //  4, 3

        StringBuilder result = new StringBuilder();

        outer:
        for (long i = 0L; i <= 4L; i++) {
            inner:
            for (long j = 0L; j <= 3L; j++) {
                // two weird rules
                if (j >= i) break inner;
                if (i == 3) continue outer;

                result.append(i);
                result.append(", ");
                result.append('\n');
            }
        }

        return result;
    }

    public static StringBuilder doLoop() {
        StringBuilder result = new StringBuilder();

        long i = 0L;
        outer:
        do {
            long j = 0L;
            inner:
            do {

                // two weird rules
                if (j >= i) break inner;
                if (i == 3) {
                    i++; // otherwise would infinite loop
                    continue outer;
                }

                result.append(i);
                result.append(", ");
                result.append('\n');

                j++;

            } while (j <= 3L);

            i++;

        } while (i <= 4L);

        return result;
    }

    public static StringBuilder whileLoop() {
        StringBuilder result = new StringBuilder();

        long i = 0L;
        outer:
        while(i <= 4L) {
            long j = 0L;
            inner:
            while (j <= 3L) {

                // two weird rules
                if (j >= i) break inner;
                if (i == 3) {
                    i++; // otherwise would infinite loop
                    continue outer;
                }

                result.append(i);
                result.append(", ");
                result.append('\n');

                j++;

            }

            i++;

        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println("For loop:");
        System.out.println(forLoop().toString());

        System.out.println("Do loop:");
        System.out.println(doLoop().toString());

        System.out.println("While loop:");
        System.out.println(whileLoop().toString());
    }
}