package MultiThreading.ReentrantLock;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates different ways of using ReentrantLock:
 * 1. tryLock() without waiting
 * 2. tryLock() with timed waiting
 * 3. Proper unlock() usage with finally block
 *
 * Note:
 * Using lock.lock() behaves similar to synchronized because
 * threads wait indefinitely. Also, unlock must be called manually.
 * In such cases, synchronized may be simpler unless fine-grained
 * control is required.
 */
public class MethodDemo {

    public static void main(String[] args) throws InterruptedException {

        // Scenario 1: Simple tryLock (no waiting)
        SimpleTryLockBankAccount simpleAccount = new SimpleTryLockBankAccount();
        executeWithdrawalScenario(simpleAccount::withdraw, 50, 10);

        // Scenario 2: tryLock with timeout
        TimedTryLockBankAccount timedAccount = new TimedTryLockBankAccount();
        executeWithdrawalScenario(timedAccount::withdraw, 50, 10);

        // Scenario 3: tryLock with proper unlock in finally
        SafeUnlockBankAccount safeAccount = new SafeUnlockBankAccount();
        executeWithdrawalScenario(safeAccount::withdraw, 50, 10);
    }

    /**
     * Utility method to remove duplicate thread creation logic.
     */
    private static void executeWithdrawalScenario(WithdrawalTask task, int amount1, int amount2)
            throws InterruptedException {

        Thread t1 = new Thread(() -> task.withdraw(amount1));
        Thread t2 = new Thread(() -> task.withdraw(amount2));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    @FunctionalInterface
    interface WithdrawalTask {
        void withdraw(int amount);
    }
}

/**
 * Demonstrates basic tryLock() usage.
 *
 * If lock is unavailable, the thread does NOT wait and moves to else block.
 * This gives better control compared to synchronized when non-blocking
 * behavior is desired.
 */
class SimpleTryLockBankAccount {

    private int balance = 100;
    private final Lock lock = new ReentrantLock();

    public void withdraw(int amount) {
        log("Trying to withdraw");

        if (lock.tryLock()) { // Non-blocking attempt
            log("Lock acquired");

            performWithdrawal(amount);

            // Intentionally not unlocking here (demonstration purpose)
            // Shows why manual unlocking is important.
        } else {
            log("Server busy - lock acquired by another thread");
        }
    }

    protected void performWithdrawal(int amount) {
        if (balance > amount) {
            balance -= amount;
            log("Amount deducted: " + amount);
        } else {
            log("Insufficient balance");
        }
    }

    protected void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}

/**
 * Demonstrates tryLock(timeout, unit).
 *
 * The thread waits for a specified time to acquire the lock.
 * If it fails within that duration, it proceeds to else block.
 */
class TimedTryLockBankAccount {

    private int balance = 100;
    private final Lock lock = new ReentrantLock();

    public void withdraw(int amount) {
        log("Trying to withdraw");

        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) { // Wait up to 3 seconds
                log("Lock acquired");

                try {
                    simulateCostlyOperation(10000);
                    performWithdrawal(amount);
                } finally {
                    // Even though original version didn’t unlock,
                    // we keep correct practice here.
                    lock.unlock();
                }

            } else {
                log("Waited 3 seconds but could not acquire lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt flag
        }
    }

    private void simulateCostlyOperation(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void performWithdrawal(int amount) {
        if (balance > amount) {
            balance -= amount;
            log("Amount deducted: " + amount);
        } else {
            log("Insufficient balance");
        }
    }

    private void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}

/**
 * Demonstrates the correct and VERY IMPORTANT usage of unlock().
 *
 * If a thread acquires a lock, it MUST release it.
 * Otherwise, other threads will never acquire the lock.
 *
 * Best practice:
 * Always place lock.unlock() inside a finally block,
 * because finally executes whether an exception occurs or not.
 */
class SafeUnlockBankAccount {

    private int balance = 100;
    private final Lock lock = new ReentrantLock();

    public void withdraw(int amount) {
        log("Trying to withdraw");

        try {
            if (lock.tryLock(3, TimeUnit.SECONDS)) {
                log("Lock acquired");

                try {
                    simulateCostlyOperation(1000);
                    performWithdrawal(amount);
                } finally {
                    // Critical: Always release lock in finally
                    lock.unlock();
                }

            } else {
                log("Waited 3 seconds but could not acquire lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // If thread was interrupted, compensation logic can go here
        if (Thread.currentThread().isInterrupted()) {
            // Handle incomplete operation if needed
        }
    }

    private void simulateCostlyOperation(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void performWithdrawal(int amount) {
        if (balance > amount) {
            balance -= amount;
            log("Amount deducted: " + amount);
        } else {
            log("Insufficient balance");
        }
    }

    private void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}
