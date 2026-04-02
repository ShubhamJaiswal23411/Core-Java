package MultiThreading.ReentrantLock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates the behavioral difference between:
 *
 * 1. lock()
 * - NOT interruptible while waiting.
 * - If a thread is blocked waiting to acquire the lock, calling interrupt()
 * does NOT stop it from waiting.
 *
 * 2. lockInterruptibly()
 * - Interruptible while waiting.
 * - If a thread is blocked waiting for the lock and is interrupted,
 * it immediately throws InterruptedException and stops waiting.
 *
 * The difference becomes visible only when a thread is BLOCKED
 * waiting to acquire a lock already held by another thread.
 *
 * Important Notes:
 * - When InterruptedException is caught, the thread's interrupt flag
 * is CLEARED. If higher-level monitoring or handling requires it,
 * the interrupt status should be restored using:
 * Thread.currentThread().interrupt();
 *
 * - When a thread is waiting indefinitely to acquire a lock using lock(),
 * its state is typically WAITING (not TIMED_WAITING), because it waits
 * until the lock becomes available.
 *
 * - lockInterruptibly() is useful for building responsive and cancellable
 * systems where waiting threads should be able to terminate early.
 */
public class InterruptibilyLockDemo {

    public static void main(String[] args) throws InterruptedException{

        System.out.println("=== Demonstrating lockInterruptibly() ===");
        demonstrateInterruptibleLock();

        Thread.sleep(2000);

        System.out.println("\n=== Demonstrating lock() (non-interruptible) ===");
        demonstrateNonInterruptibleLock();
    }

    private static void demonstrateInterruptibleLock() throws InterruptedException {
        Lock lock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Holder thread acquired lock and sleeping...");
                sleep(5000);
            } finally {
                System.out.println("Holder thread released lock");
                lock.unlock();
            }
        });

        Thread waiter = new Thread(() -> {
            try {
                System.out.println("Waiter trying lockInterruptibly...");
                lock.lockInterruptibly(); // interruptible wait
                try {
                    System.out.println("Waiter acquired lock");
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println("Waiter was INTERRUPTED while waiting!");
            }
        });

        holder.start();
        Thread.sleep(500); // ensure holder acquires lock first
        waiter.start();

        Thread.sleep(1000); // let waiter start waiting
        System.out.println(waiter.getState()); // WAITING not TIMED_WAITING unlike tryLock with time.
        waiter.interrupt(); // interrupt while blocked

        holder.join();
        waiter.join();
    }

    private static void demonstrateNonInterruptibleLock() throws InterruptedException {
        Lock lock = new ReentrantLock();

        Thread holder = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Holder thread acquired lock and sleeping...");
                sleep(5000);
            } finally {
                lock.unlock();
                System.out.println("Holder thread released lock");
            }
        });

        Thread waiter = new Thread(() -> {
            System.out.println("Waiter trying lock()...");
            try {
                lock.lock(); // NOT interruptible while waiting
                try {
                    System.out.println("Waiter acquired lock (after holder released)");
                } finally {
                    lock.unlock();
                }
            } catch (Exception e) {
                System.out.println("Waiter was INTERRUPTED while waiting!");
            }
        });

        holder.start();
        Thread.sleep(500);
        waiter.start();

        Thread.sleep(1000);
        System.out.println("Calling waiter.interrupt() but nothing would happen to its state");
        waiter.interrupt(); // This does NOTHING while waiting
        System.out.println(waiter.getState());
        holder.join();
        waiter.join();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
