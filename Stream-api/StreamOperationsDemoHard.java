import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamOperationsDemoHard {

    /*
     * 1. Given a list of integers, find the top 3 largest numbers.
     * 2. Given a list of integers, find the kth smallest element.
     * 3. Given a list of strings, group them by first character.
     * 4. Given a list of integers, find the sum of squares of even numbers only.
     * 5. Given a list of strings, find all anagrams grouped together.
     * 6. Given a list of integers, find the first non-repeating element.
     * 7. Given a list of strings, find the most frequent word.
     */

    public static void main(String[] args) {

        List<Integer> numberList = List.of(2, 3, 1, 4, 56, 2, 3, 5, 8, 11, 12, 22, 9);
        List<String> stringList = List.of("as", "As", "s", "as","as", "s", "sdfs", "sdf","sdfsdfsd", "sdfsf", "we", "er", "wee", "werwr", "gjk","jkg","eew");
        
        //max 3 elements
        List<Integer> max3Elements = numberList.stream().sorted(Collections.reverseOrder()).limit(3).toList();
        System.out.println(max3Elements);


        //find kth smallest integer 
        int k =6;
        Integer kthSmallest = numberList.stream().sorted().skip(k-1).findFirst().orElse(null);
        Integer kthSmallest2 = numberList.stream().sorted().limit(k).reduce((a,b)->b).orElse(null);
        System.out.println(kthSmallest+"--"+kthSmallest2);


        //group based on first character.
        Map<Character, List<String>> firstCharGroupingList = stringList.stream().collect(Collectors.groupingBy(x -> x.charAt(0)));
        System.out.println(firstCharGroupingList);  

        //find sum of even squares
        int sumOfSquaresOfEvenNumbers = numberList.stream().filter(x->x%2==0).map(x->x*x).mapToInt(Integer::intValue).sum();
        System.out.println(sumOfSquaresOfEvenNumbers);


        //group anagrams together
        Map<String, List<String>> anagramGroup = stringList.stream().collect(Collectors.groupingBy(x->{
            char[] anagram = x.toCharArray();
            Arrays.sort(anagram);
            return new String(anagram);
        }));
        System.out.println(anagramGroup);
        

        //First Non Repeating value
        Integer firstNonRepeatingValue = numberList.stream().collect(Collectors.groupingBy(Integer::intValue, LinkedHashMap::new, Collectors.counting()))
        .entrySet().stream().filter(x->x.getValue()==1).map(Map.Entry::getKey).findFirst().orElse(null);
        System.out.println(firstNonRepeatingValue);


        String mostFrequentString = stringList.stream().collect(Collectors.groupingBy(String::valueOf, Collectors.counting()))
        .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
        System.out.println(mostFrequentString);

       




    }

}
