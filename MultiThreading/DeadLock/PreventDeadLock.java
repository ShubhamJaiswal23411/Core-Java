package DeadLock;

/**
 * Demonstrates how consistent lock ordering prevents deadlock.
 *
 * Deadlock occurs when:
 * - Thread-1 acquires Lock-A and waits for Lock-B
 * - Thread-2 acquires Lock-B and waits for Lock-A
 * → Circular wait condition forms.
 *
 * Prevention Strategy Used Here: Consistent Lock Ordering
 *
 * All threads acquire locks in the SAME order:
 *     1. Pen lock
 *     2. Paper lock
 *
 * Why this works:
 * - All threads first compete for the Pen lock.
 * - Only one thread can acquire it.
 * - Since other threads cannot acquire the first lock,
 *   they never proceed to acquire the second lock.
 * - This removes the circular wait condition.
 *
 * If we did NOT enforce consistent ordering:
 * - Different threads could acquire different locks first.
 * - Each would wait for the other to release its lock.
 * - That situation causes deadlock.
 */
public class PreventDeadLock {

    public static void main(String[] args) throws InterruptedException {

        Paper paper = new Paper();
        Pen pen = new Pen();

        Thread t1 = new Thread(() -> {
            // Enforcing consistent ordering:
            // Always acquire Pen lock first, then Paper.
            synchronized (pen) {
                paper.havePaperNeedPen(pen);
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            // This method is synchronized, so it also
            // acquires Pen lock first before accessing Paper.
            pen.havePenNeedPaper(paper);
        }, "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}

/**
 * Represents a shared Pen resource.
 *
 * All methods are synchronized, meaning they acquire
 * the intrinsic monitor lock of the Pen object.
 */
class Pen {

    /**
     * Acquires Pen lock first (method is synchronized),
     * then accesses Paper.
     *
     * Since all threads acquire Pen lock first,
     * lock ordering remains consistent.
     */
    public synchronized void havePenNeedPaper(Paper paper) {
        log("Acquired Pen lock. Now accessing Paper...");
        paper.getPaper(); // Safe because ordering is consistent
    }

    public synchronized void getPen() {
        log("Providing Pen");
    }

    private void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}

/**
 * Represents a shared Paper resource.
 *
 * Methods are synchronized to ensure thread-safe access.
 */
class Paper {

    /**
     * This method is synchronized on Paper,
     * but it is only called after Pen lock is acquired.
     *
     * Because the outer code always locks Pen first,
     * circular waiting is prevented.
     */
    public synchronized void havePaperNeedPen(Pen pen) {
        log("Acquired Paper lock. Now accessing Pen...");
        pen.getPen();
    }

    public synchronized void getPaper() {
        log("Providing Paper");
    }

    private void log(String message) {
        System.out.println(Thread.currentThread().getName() + " - " + message);
    }
}