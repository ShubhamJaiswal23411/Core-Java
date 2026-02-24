/*
 * ImmutabilityViolationDemo
 *
 * SUMMARY:
 * This program demonstrates how immutability can be broken when an immutable
 * class is allowed to be extended.
 *
 * CORE IDEA:
 * - The Person class is designed to be immutable:
 *      • It has a final field.
 *      • It does not provide any setters.
 * - However, it is NOT declared as final.
 * - Because of this, a child class can extend it and change behavior.
 *
 * WHAT THIS PROGRAM SHOWS:
 * - A subclass (MutablePerson) overrides getName().
 * - The subclass introduces state that can change after construction.
 * - Even though the parent class contract promises immutability,
 *   the child class violates that contract.
 *
 * WHY THIS IS A PROBLEM:
 * - According to the Liskov Substitution Principle (LSP),
 *   a child class should be substitutable for its parent.
 * - Any function that accepts a Person should rely on Person's guarantees.
 * - If Person promises immutability, subclasses must also honor it.
 * - But here, the subclass changes the state after object creation.
 *
 * RESULT:
 * - The object is created with name "john".
 * - It is later changed to "todd".
 * - A method expecting an immutable Person would assume the name never changes.
 * - This breaks the immutability contract.
 *
 * CONCLUSION:
 * - Immutable classes should generally be declared final.
 * - This prevents subclasses from overriding behavior and violating the contract.
 */

public class ImmutabilityViolationDemo {

    public static void main(String[] args) {

        // A Person reference pointing to a MutablePerson object
        Person person = new MutablePerson("john");

        // Downcasting to access subclass-specific behavior
        ((MutablePerson) person).setName("todd");

        // Even though Person is supposed to be immutable,
        // the name has changed after object creation.
        System.out.println(person.getName());
    }
}

/*
 * Intended Immutable Class
 *
 * This class appears immutable because:
 * - The field is final.
 * - No setter methods exist.
 *
 * BUT:
 * - It is not declared final.
 * - Therefore, it can be extended.
 * - Subclasses can override behavior and break immutability.
 */
class Person {

    private final String name;

    public Person(String name) {
        System.out.println("Person Constructor");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

/*
 * Subclass That Breaks Immutability
 *
 * - Overrides getName()
 * - Introduces mutable state
 * - Provides a setter
 * - Violates the immutability contract of the parent class
 */
class MutablePerson extends Person {

    private String mutableName;

    public MutablePerson(String name) {
        super(name);
        System.out.println("MutablePerson Constructor");
        this.mutableName = name;
    }

    @Override
    public String getName() {
        return mutableName;
    }

    public void setName(String name) {
        this.mutableName = name;
    }
}