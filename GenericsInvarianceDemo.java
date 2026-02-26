import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates invariance in Java Generics.
 *
 * Key Concept:
 * - Generic types in Java are invariant.
 * - Even though Integer is a subclass of Number,
 *   List<Integer> is NOT a subclass of List<Number>.
 *
 * This prevents type-safety issues at compile time.
 */
public class GenericsInvarianceDemo {

    public static void main(String[] args) {

        // Normal inheritance works as expected
        Number number = Integer.valueOf(0);  // Integer is a subclass of Number

        // Generic types must match exactly
        List<Integer> integerList = new ArrayList<>();
        List<List<Integer>> twoDimensionalList = new ArrayList<>();
        twoDimensionalList.add(new ArrayList<>());

        /*
         * The following line does NOT compile:
         *
         * List<Number> numbers = new ArrayList<Integer>();
         *
         * Even though:
         * - Integer extends Number
         * - ArrayList implements List
         *
         * This is because generics in Java are invariant.
         */

        /*
         * Why is this restriction necessary?
         *
         * Suppose List<Integer> could be assigned to List<Number>.
         * Then we could do this:
         *
         * List<Number> numbers = new ArrayList<Integer>();
         * numbers.add(3.14);  // Double is a Number
         *
         * But the underlying list actually stores Integer objects.
         * Adding a Double would break type safety.
         *
         * To prevent this problem, Java makes generic types invariant.
         */
    }
}