package ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates why ReentrantLock is called "reentrant".
 *
 * A reentrant lock allows the SAME thread to acquire the same lock
 * multiple times without causing deadlock.
 *
 * Important:
 * - If a thread acquires the lock N times, it must release it N times.
 * - Thread.holdsLock(object) works only with monitor locks (synchronized),
 *   not with manual locks like ReentrantLock.
 */
public class ReentrancyDemo {

    private final Lock reentrantLock = new ReentrantLock();

    /**
     * Attempts to acquire the lock and then calls another method
     * that tries to acquire the SAME lock again.
     *
     * This works because ReentrantLock is reentrant.
     */
    public void outerOperation() {
        boolean lockAcquired = false;

        try {
            log("Outer: Trying to acquire lock");
            lockAcquired = reentrantLock.tryLock(); // Non-blocking attempt

            if (lockAcquired) {
                log("Outer: Lock acquired");
                innerOperation(); // Same thread tries again (reentrancy)
            }
        } finally {
            // Only unlock if THIS thread successfully acquired it
            if (lockAcquired) {
                reentrantLock.unlock();
                log("Outer: Lock released");
            } else {
                log("Outer: Could not acquire lock");
            }
        }
    }

    /**
     * Attempts to acquire the same lock again.
     *
     * Since the same thread already owns the lock from outerOperation(),
     * tryLock() succeeds and increases the hold count.
     */
    public void innerOperation() {
        boolean lockAcquired = false;

        try {
            lockAcquired = reentrantLock.tryLock(); // Reentrant acquisition

            if (lockAcquired) {
                log("Inner: Lock acquired (reentrant)");
            }
        } finally {
            // CRITICAL:
            // Unlock ONLY if this method successfully acquired it.
            // If we skip this check, we may cause IllegalMonitorStateException. 
            // because this funciton can be called directly as well and if we dont 
            // check if lockAcquired then we might be unlocking a lock which isnt even acquired 
            if (lockAcquired) {
                reentrantLock.unlock();
                log("Inner: Lock released");
            }
        }
    }

    /**
     * Utility method for consistent thread logging.
     */
    private void log(String message) {
        System.out.println(message + " - " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {

        ReentrancyDemo demo = new ReentrancyDemo();

        Thread t1 = new Thread(demo::outerOperation);
        Thread t2 = new Thread(()->demo.outerOperation());
        //Notice the usage of method reference and lambda both are doing the same thing.

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}