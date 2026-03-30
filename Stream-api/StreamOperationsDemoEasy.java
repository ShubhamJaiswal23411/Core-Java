import java.util.*;

    /*
     * 1. Given a list of integers, return all even numbers.
     * 2. Given a list of integers, return all numbers greater than 10.
     * 3. Given a list of strings, convert all strings to uppercase.
     * 4. Given a list of strings, return only strings with length > 5.
     * 5. Given a list of integers, return the square of each number.
     * 6. Given a list of integers, find the sum of all elements.
     * 7. Given a list of integers, find the maximum element.
     * 8. Given a list of integers, find the minimum element.
     * 9. Given a list of strings, count how many strings start with 'A'.
     * 10. Given a list of integers, remove all duplicate elements.
     * 11. Given a list of strings, sort them in ascending order.
     * 12. Given a list of strings, sort them in decending order.
     * 13. Given a list of integers, check if any number is divisible by 5.
     */

public class StreamOperationsDemoEasy {

    public static void main(String[] args) {

        List<Integer> numberList = List.of(2, 3, 1, 4, 56, 2, 3, 5, 8, 11, 12, 22);
        List<String> stringList = List.of(
                "as", "As", "s", "as", "s", "sdfs", "sdf",
                "sdfsdfsd", "sdfsf", "we", "er", "wee", "werwr", "gjk"
        );

        // 1. Filter even numbers
        List<Integer> evenNumbers = numberList.stream()
                .filter(num -> num % 2 == 0)
                .toList();
        System.out.println(evenNumbers);

        // 2. Filter numbers greater than 10
        List<Integer> greaterThanTen = numberList.stream()
                .filter(num -> num > 10)
                .toList();
        System.out.println(greaterThanTen);

        // 3. Convert all strings to uppercase
        List<String> upperCaseStrings = stringList.stream()
                .map(String::toUpperCase)
                .toList();
        System.out.println(upperCaseStrings);

        // 4. Filter strings with length > 5
        List<String> longStrings = stringList.stream()
                .filter(str -> str.length() > 5)
                .toList();
        System.out.println(longStrings);

        // 5. Square each number
        List<Integer> squaredNumbers = numberList.stream()
                .map(num -> num * num)
                .toList();
        System.out.println(squaredNumbers);

        // 6. Sum of all elements (multiple approaches)

        // Approach 1: reduce without identity (unsafe for empty list)
        Integer sumUsingReduce = numberList.stream()
                .reduce((a, b) -> a + b)
                .get(); // may throw exception if list is empty
        System.out.println(sumUsingReduce);

        // Approach 2: reduce with identity (safe)
        Integer sumWithIdentity = numberList.stream()
                .reduce(0, (a, b) -> a + b);
        System.out.println(sumWithIdentity);

        // Approach 3: method reference (cleaner functional style)
        Integer sumUsingMethodRef = numberList.stream()
                .reduce(0, Integer::sum);
        System.out.println(sumUsingMethodRef);

        // Approach 4: using primitive stream (most efficient)
        int sumUsingIntStream = numberList.stream()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println(sumUsingIntStream);

        // 7. Find maximum element

        // Custom reduce logic
        Integer maxUsingReduce = numberList.stream()
                .reduce(Integer.MIN_VALUE, (a, b) -> a < b ? b : a);
        System.out.println(maxUsingReduce);

        // Using IntStream
        int maxUsingIntStream = numberList.stream()
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();
        System.out.println(maxUsingIntStream);

        // Using built-in comparator
        Integer maxUsingComparator = numberList.stream()
                .max(Integer::compareTo)
                .get();
        System.out.println(maxUsingComparator);

        // Using method reference with reduce
        Integer maxUsingMethodRef = numberList.stream()
                .reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println(maxUsingMethodRef);

        // 8. Find minimum element

        Integer minUsingReduce = numberList.stream()
                .reduce(Integer.MAX_VALUE, (a, b) -> a > b ? b : a);
        System.out.println(minUsingReduce);

        int minUsingIntStream = numberList.stream()
                .mapToInt(Integer::intValue)
                .min()
                .getAsInt();
        System.out.println(minUsingIntStream);

        Integer minUsingComparator = numberList.stream()
                .min(Integer::compareTo)
                .get();
        System.out.println(minUsingComparator);

        Integer minUsingMethodRef = numberList.stream()
                .reduce(Integer.MAX_VALUE, Integer::min);
        System.out.println(minUsingMethodRef);

        // 9. Count strings starting with 'A' (case-insensitive)

        // Manual character check
        long countStartsWithA = stringList.stream()
                .filter(str -> str.charAt(0) == 'a' || str.charAt(0) == 'A')
                .count();
        System.out.println(countStartsWithA);

        // Cleaner approach using lowercase conversion
        long countUsingStartsWith = stringList.stream()
                .filter(str -> str.toLowerCase().startsWith("a"))
                .count();
        System.out.println(countUsingStartsWith);

        // 10. Remove duplicates
        List<Integer> distinctNumbers = numberList.stream()
                .distinct()
                .toList();
        System.out.println(distinctNumbers);

        // 11. Sort strings in ascending order
        List<String> sortedAscending = stringList.stream()
                .sorted()
                .toList();
        System.out.println(sortedAscending);

        // Explicit comparator (same as above)
        List<String> sortedAscendingComparator = stringList.stream()
                .sorted(String::compareTo)
                .toList();
        System.out.println(sortedAscendingComparator);

        // 12. Sort strings in descending order
        List<String> sortedDescending = stringList.stream()
                .sorted(Collections.reverseOrder())
                .toList();
        System.out.println(sortedDescending);

        // 13. Check if any number is divisible by 5

        // Less efficient (counts all matches)
        boolean isDivisibleByFive = numberList.stream()
                .filter(num -> num % 5 == 0)
                .count() > 0;
        System.out.println(isDivisibleByFive);

        // Better approach (short-circuits early)
        boolean isDivisibleByFiveOptimized = numberList.stream()
                .anyMatch(num -> num % 5 == 0);
        System.out.println(isDivisibleByFiveOptimized);
    }
}