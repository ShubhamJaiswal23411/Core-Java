import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamOpeationsDemoMedium {
        /*
         * 1. Given a list of integers, return the first even number.
         * 2. Given a list of integers, return the last element using streams.
         * 3. Given a list of integers, return numbers sorted in descending order.
         * 4. Given a list of strings, group them by their length.
         * count them by their lenght.
         * group them in to a set to duplicates are removed
         * individual groups should also be sorted and grouped based on lenght.
         * 5. Given a list of integers, partition them into even and odd groups.
         * count them by their lenght.
         * group them in to a set to duplicates are removed
         * individual groups should also be sorted and grouped based on lenght.
         * 6. Given a list of integers, find the second highest number.
         * 7. Given a list of integers, find the frequency of each element.
         * 8. Given a list of strings, join them into a single comma-separated string.
         * 9. Given a list of integers, find numbers that are perfect squares.
         * 10. Given a list of integers, filter numbers between 20 and 50, then sort.
         * 11. Given a list of strings, find the longest string.
         * 12. Given a list of integers, compute the average of all numbers.
         * 13. Given a list of integers, find all numbers that appear more than once.
         * 14. Given a list of strings, remove duplicates and return them sorted.
         * sorted in case insensitive.
         * 15. Given a list of integers, multiply all elements together (product).
         * 16. Given a list of strings, count occurrences of each word.
         * 
         */

        public static void main(String[] args) {
                List<Integer> numberList = List.of(2, 3, 1, 4, 56, 2, 3, 5, 8, 11, 12, 22, 9);
                List<String> stringList = List.of(
                                "as", "As", "s", "as", "s", "sdfs", "sdf",
                                "sdfsdfsd", "sdfsf", "we", "er", "wee", "werwr", "gjk");

                Integer firstEven = numberList.stream().filter(n -> n % 2 == 0).findFirst().orElse(null);
                System.out.println(firstEven);

                // find last element
                Integer lastValue = numberList.stream().skip(numberList.size() - 1).findFirst().orElse(null);
                System.out.println(lastValue);
                Integer lastValue2 = numberList.stream().reduce((a, b) -> b).orElse(null);
                System.out.println(lastValue2);

                // sort
                List<Integer> reverseSortedList = numberList.stream().sorted(Collections.reverseOrder()).toList();
                System.out.println(reverseSortedList);

                // group by length
                Map<Integer, List<String>> groupByLength = stringList.stream()
                                .collect(Collectors.groupingBy(String::length));
                System.out.println(groupByLength);

                // we can count as well instead of grouping
                Map<Integer, Long> countLengthMapping = stringList.stream()
                                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
                System.out.println(countLengthMapping);

                // we can also use set instead of a list which is default
                Map<Integer, Set<String>> groupInSet = stringList.stream()
                                .collect(Collectors.groupingBy(String::length, Collectors.toSet()));
                System.out.println(groupInSet);

                // group by using length and then sort each group as well
                stringList.stream()
                .collect(Collectors
                .groupingBy(String::length, Collectors
                .collectingAndThen(Collectors.toList(), list -> list
                        .stream().sorted().toList())));
                // group in to even and odd group
                Map<String, List<Integer>> evenOddGroup = numberList.stream()
                                .collect(Collectors.groupingBy(x -> x % 2 == 0 ? "even" : "odd"));
                System.out.println(evenOddGroup);
                // group into even and odd and remove duplicates as well
                Map<String, Set<Integer>> oddEvenSet = numberList.stream()
                                .collect(Collectors.groupingBy(x -> x % 2 == 0 ? "even" : "odd", Collectors.toSet()));
                System.out.println(oddEvenSet);
                // group into odd even and sort numbers
                Map<String, List<Integer>> groupOddEvenAndSort = numberList.stream()
                                .collect(Collectors.groupingBy(x -> x % 2 == 0 ? "even" : "odd",
                                                Collectors.collectingAndThen(Collectors.toList(),
                                                                list -> list.stream().sorted().toList())));
                System.out.println(groupOddEvenAndSort);

                // second highest number
                Integer secondLargest = numberList.stream().distinct().sorted(Collections.reverseOrder()).skip(1)
                                .findFirst()
                                .orElse(null);
                System.out.println(secondLargest);
                Integer secondLargest2 = numberList.stream().distinct().sorted(Collections.reverseOrder()).limit(2)
                                .reduce((a, b) -> b).orElse(null);
                System.out.println(secondLargest2);
                Integer secondLargest3 = numberList.stream().distinct().sorted(Collections.reverseOrder()).limit(2)
                                .sorted()
                                .findFirst().orElse(null);
                System.out.println(secondLargest3);

                // frequency of each element
                Map<Integer, Long> mapIntegerCount = numberList.stream()
                                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()));
                System.out.println(mapIntegerCount);

                // joining strings
                String joinedString = stringList.stream().reduce("", (a, b) -> a + b);
                System.out.println(joinedString);
                String joinedString2 = stringList.stream().reduce("", String::concat);
                System.out.println(joinedString2);

                // join string comma separated
                String commaJoinedString = stringList.stream().collect(Collectors.joining(", "));
                System.out.println(commaJoinedString);

                // find perfect Squares
                List<Integer> perfectSquares = numberList.stream().filter(x -> {
                        int sqrt = (int) Math.sqrt((double) x);
                        return (sqrt * sqrt) == x;
                }).toList();

                System.out.println(perfectSquares);

                // filter and sort number between 20 and 50
                List<Integer> sortedList = numberList.stream().filter(x -> (x >= 20 && x <= 50)).sorted().toList();
                System.out.println(sortedList);

                // find longest string
                String largestString = stringList.stream().reduce((a, b) -> a.length() < b.length() ? b : a)
                                .orElse(null);
                System.out.println(largestString);

                // better version
                String largestString2 = stringList.stream().max(Comparator.comparingInt(String::length)).orElse(null);
                System.out.println(largestString2);

                // find avg of all numbers
                double avg = numberList.stream().mapToInt(Integer::intValue).average().orElse(0);
                System.out.println(avg);

                // find all the numbers that exist more than once
                // here we are actually using an important concept which is creating two streams
                // to pipelining
                // streams themselves when we call collect first stream is finished and then we
                // create another
                // stream from the map given by the first stream using entrySet() and then we
                // can just filter
                List<Entry<Integer, Long>> duplicateList = numberList.stream()
                                .collect(Collectors.groupingBy(Integer::intValue, Collectors.counting()))
                                .entrySet().stream()
                                .filter(x -> x.getValue() > 1)
                                .toList();
                System.out.println(duplicateList);

                // another beeautiful way of doing above thing
                HashSet<Integer> seen = new HashSet<>();
                List<Integer> duplicateList2 = numberList.stream()
                                .filter(x -> !seen.add(x))
                                .distinct()
                                .toList();
                System.out.println(duplicateList2 +"=================");

                // distinct sorted list
                List<String> distinctSortedList = stringList.stream().distinct().sorted().toList();
                System.out.println(distinctSortedList);

                List<String> distinctSortedList_caseInsensitive = stringList.stream().distinct()
                                .sorted(String.CASE_INSENSITIVE_ORDER).toList();
                System.out.println(distinctSortedList_caseInsensitive);

                // find product of an array
                double product = numberList.stream().mapToDouble(Double::valueOf).reduce((a, b) -> a * b).orElse(0D);
                System.out.println(product);

                Map<String, Long> wordOccurances = stringList.stream()
                                .collect(Collectors.groupingBy(String::valueOf, Collectors.counting()));
                System.out.println(wordOccurances);

        }

}
