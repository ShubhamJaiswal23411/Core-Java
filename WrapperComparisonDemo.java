import java.util.ArrayList;
import java.util.Collection;

/**
 * Demonstrates how comparisons work between:
 * 1. Primitive types and wrapper classes
 * 2. Wrapper objects inside and outside the Integer cache range
 *
 * Key Concepts:
 * - When a primitive (int) is compared with an Integer object using ==,
 *   Java automatically unboxes the Integer to int, so values are compared.
 *
 * - Integer objects between -128 and 127 are cached by Java.
 *   This means the same object reference is reused within this range.
 *
 * - Integer objects outside this range are NOT cached,
 *   so different objects are created even if the values are equal.
 */
public class WrapperComparisonDemo {

    public static void main(String[] args) {

        // Primitive vs Wrapper comparison (auto-unboxing happens)
        int primitiveValue = 300;
        Integer wrapperValue = 300;

        // The Integer is unboxed to int, so values are compared
        System.out.println(primitiveValue == wrapperValue);  // true


        // Wrapper comparison within cache range (-128 to 127)
        Integer cachedNumberOne = 127;
        Integer cachedNumberTwo = 127;

        // Both references point to the same cached object
        System.out.println(cachedNumberOne == cachedNumberTwo);  // true


        // Wrapper comparison outside cache range
        Integer nonCachedNumberOne = 128;
        Integer nonCachedNumberTwo = 128;

        // Different objects are created, so references are different
        System.out.println(nonCachedNumberOne == nonCachedNumberTwo);  // false
        
    }
}