
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicVsNormalCounterDemo
 *
 * This class demonstrates the difference between:
 * 1. AtomicInteger (thread-safe)
 * 2. A normal int counter (not thread-safe)
 *
 * Two threads increment the counter 1000 times each.
 *
 * With AtomicInteger:
 * - incrementAndGet() is atomic (uses CAS internally).
 * - No increments are lost.
 * - Final result is guaranteed to be 2000.
 *
 * With a normal int:
 * - count++ is NOT atomic (read → modify → write).
 * - Race conditions may occur.
 * - Some increments may be lost.
 * - Final result may be less than 2000.
 *
 * We use join() to:
 * - Ensure both threads complete
 * - Establish a happens-before relationship
 * - Guarantee visibility of updates to the main thread
 */
public class AtomicVsNormalCounterDemo {

    private AtomicInteger atomicCounter = new AtomicInteger(0);

    public void incrementAtomic() {
        atomicCounter.incrementAndGet();
    }

    public int getAtomicValue() {
        return atomicCounter.get();
    }

    public static void main(String[] args) throws InterruptedException {

        // ----------------------------
        // Atomic Counter Demonstration
        // ----------------------------
        AtomicVsNormalCounterDemo atomicDemo = new AtomicVsNormalCounterDemo();

        Thread atomicThread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicDemo.incrementAtomic();
            }
        });

        Thread atomicThread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                atomicDemo.incrementAtomic();
            }
        });

        atomicThread1.start();
        atomicThread2.start();

        // Ensures both threads finish before printing result
        atomicThread1.join();
        atomicThread2.join();

        System.out.println("Atomic Counter Result: " + atomicDemo.getAtomicValue());

        // ----------------------------
        // Normal Counter Demonstration
        // ----------------------------
        SimpleCounter normalCounter = new SimpleCounter();

        Thread normalThread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                normalCounter.increment();
            }
        });

        Thread normalThread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                normalCounter.increment();
            }
        });

        normalThread1.start();
        normalThread2.start();

        normalThread1.join();
        normalThread2.join();

        System.out.println("Normal Counter Result: " + normalCounter.getValue());
    }
}

/**
 * SimpleCounter
 *
 * A non-thread-safe counter implementation.
 *
 * The increment operation:
 * count++
 *
 * Is internally equivalent to:
 * 1. Read current value
 * 2. Add 1
 * 3. Write new value
 *
 * When multiple threads execute this simultaneously,
 * lost updates can occur because these steps are not atomic.
 */
class SimpleCounter {

    private int count = 0;

    public void increment() {
        count++; // Not atomic
    }

    public int getValue() {
        return count;
    }
}