
/*
 * Rules to make a class immutable :
 * 1. Make the class `final`
 * 2. Make all fields `private` and `final`
 * 3. No setters
 * 4. Initialize fields through constructor
 * 5. Very Important point : Perform defensive copy for mutable fields even when setting in the constructors 
 * 6. Return defensive copies in getters
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class WrongImmutable {

    private final int age;
    private final String address;
    private final List<Integer> marks;// mutable colleciton that can still be changed if returned directly. so we
                                      // return a deep copy

    public WrongImmutable(int age, String address, List<Integer> marks) {
        this.age = age;
        this.address = address;
        this.marks = marks;// original reference is still there so this marks can be changed even after
                           // object creatation.
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public List<Integer> getMarks() {
        return new ArrayList<>(marks);
    }

}

public final class Immutable {

    private final int age;
    private final String address;
    private final List<Integer> marks;

    public Immutable(int age, String address, List<Integer> marks) {
        this.age = age;
        this.address = address;
        this.marks = new ArrayList<>(marks);// creating a new list even when creating an object
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public List<Integer> getMarks() {
        return new ArrayList<>(marks);
    }

}

class demo {
    public static void main(String[] args) {
        // wrong Immutable
        List<Integer> markList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 8));

        WrongImmutable object = new WrongImmutable(5, "new delhi", markList);
        markList.add(9);
        List<Integer> objectMarks = object.getMarks();
        System.out.println(objectMarks);// original list is updated using the refernce so breaking immutablity.
        System.out.println(objectMarks == markList);
        // false, even though the list we got is a deep copy the original list's
        // reference is still stored in marklist

        // CorrectImmutablity
        Immutable immutableObject = new Immutable(122, "bangalore", markList);
        markList.add(10);// this 10 should not appear in the marks for immutableObject
        List<Integer> immutableMarks = immutableObject.getMarks();
        System.out.println(immutableMarks);
        System.out.println(immutableMarks == markList);

    }
}
