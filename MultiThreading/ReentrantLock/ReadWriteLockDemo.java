package MultiThreading.ReentrantLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Demonstrates how ReentrantReadWriteLock works.
 *
 * A ReentrantReadWriteLock internally maintains a pair of synchronized locks:
 * - Read Lock → Multiple threads can acquire simultaneously.
 * - Write Lock → Exclusive; only one writer allowed.
 *
 * Important Behavior:
 * - Multiple readers can read at the same time.
 * - If a writer acquires the write lock, no readers can acquire the read lock.
 * - If readers are active, a writer must wait until all readers release the
 * lock.
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) throws InterruptedException {

        List<Thread> writerThreads = new ArrayList<>();
        List<Thread> readerThreads = new ArrayList<>();

        SharedCounter sharedCounter = new SharedCounter();

        // Create reader and writer threads
        for (int i = 0; i < 1000; i++) {
            if (i % 2 == 0) {
                writerThreads.add(
                        new Thread(sharedCounter::incrementCounter, "Writer-" + i));
            }
            readerThreads.add(
                    new Thread(sharedCounter::readCounter, "Reader-" + i));
        }

        // Start readers first
        for (Thread reader : readerThreads) {
            reader.start();
        }

        // Then start writers
        for (Thread writer : writerThreads) {
            writer.start();
        }

        // Wait for writers to finish
        for (Thread writer : writerThreads) {
            writer.join();
        }

        // Wait for readers to finish
        for (Thread reader : readerThreads) {
            reader.join();
        }
    }
}

/**
 * Represents a shared counter protected by ReentrantReadWriteLock.
 *
 * - Read operations use the read lock (shared access).
 * - Write operations use the write lock (exclusive access).
 *
 * Ensures data consistency while allowing higher concurrency
 * compared to using a single mutual exclusion lock.
 */
class SharedCounter {

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    // Separate read and write locks obtained from the same ReadWriteLock
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    private int counter = 0;

    /**
     * Increments the counter.
     * Must acquire the write lock because it modifies shared state.
     */
    public void incrementCounter() {
        writeLock.lock(); // Exclusive access
        try {
            counter++;
            log("Incremented counter to " + counter);
        } finally {
            // Critical: Always release write lock
            writeLock.unlock();
        }
    }

    /**
     * Reads the current counter value.
     * Uses read lock, allowing multiple concurrent readers.
     */
    public void readCounter() {
        readLock.lock(); // Shared access
        try {
            log("Reading counter value: " + counter);
        } finally {
            // Always release read lock
            readLock.unlock();
        }
    }

    private void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}