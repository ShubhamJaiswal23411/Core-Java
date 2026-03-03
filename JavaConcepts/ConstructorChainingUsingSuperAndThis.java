package JavaConcepts;

/**
 * Demonstrates how `this()` and `super()` behave inside constructors.
 *
 * Key Rules:
 * 1. If nothing is written, compiler inserts `super()` as the first statement.
 * 2. If we manually write `super()`, behavior remains the same.
 * 3. `this()` calls another overloaded constructor of the same class.
 *    It cannot call itself (would cause infinite recursion).
 * 4. Even when `this()` is used, compiler eventually ensures `super()` 
 *    is called in a constructor that doesn't use `this()`.
 * 5. Both `this()` and `super()` must be the first statement in a constructor.
 * 6. If `super()` is written explicitly, `this()` cannot be written (and vice versa).
 * 7. If parent has no no-arg constructor, child must explicitly call
 *    the parameterized `super(...)`.
 * 8. Cyclic constructor calls using only `this()` cause compilation error
 *    because it also causes an infinite recursion because all the constructors will 
 *    keep calling each other.
 */
public class ConstructorChainingUsingSuperAndThis extends AbstractClass {

    /**
     * Default constructor.
     * Implicitly calls super() as the first statement.
     */
    public ConstructorChainingUsingSuperAndThis() {
        // this(1); casues infinite recursion.
        System.out.println("this is test class");
    }
    //Causes infinite recursion
    // public ThisAndSuperInConstructor(int i ){
    //     this();
    //     System.out.println("this is test class with int -"+i);
    // }

    public static void main(String[] args) {
        // Object creation triggers constructor chain:
        // NormalClass -> AbstractClass(int) -> AbstractClass() -> ThisAndSuperInConstructor()
        ConstructorChainingUsingSuperAndThis test = new ConstructorChainingUsingSuperAndThis();
    }
}


/**
 * Abstract class extending NormalClass.
 * Demonstrates constructor chaining using `this()` and implicit `super()`.
 */
abstract class AbstractClass extends NormalClass {

    /**
     * No-arg constructor.
     * Calls overloaded constructor using `this(2)`.
     */
    public AbstractClass() {
        this(2); // Must be first statement. Calls parameterized constructor.
        System.out.println("this is abstract class's constructor");
    }

    /**
     * Parameterized constructor.
     * Since it does NOT call `this()`, compiler inserts `super()` here.
     */
    public AbstractClass(int i) {
        // super() is inserted here automatically (calls NormalClass constructor)
        System.out.println("this is paramterized constructor - " + i);
    }
}


/**
 * Base parent class in the hierarchy.
 * Its constructor is ultimately called first in the chain.
 */
class NormalClass {

    /**
     * Default constructor.
     */
    public NormalClass() {
        System.out.println("this is normal class's constructor");
    }
}