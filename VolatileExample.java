public class VolatileExample {
    public static void main(String[] args) {
        // Run thread without volatile
        WorkerThreadRegular regularThread = new WorkerThreadRegular();
        regularThread.setName("RegularThread");
        regularThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        regularThread.stopWorker();

        // Run thread with volatile
        WorkerThreadVolatile volatileThread = new WorkerThreadVolatile();
        volatileThread.setName("VolatileThread");
        volatileThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        volatileThread.stopWorker();
    }
}

/**
 * WorkerThreadRegular demonstrates a thread that uses a normal boolean flag to control
 * its running state. Without the 'volatile' keyword, the JVM or CPU might cache the
 * boolean value in a register or thread-local memory. This may cause the thread to
 * continue running even after the flag is set to false by another thread, because
 * it may not immediately see the updated value. We also measure the time this thread
 * runs using System.currentTimeMillis().
 */
class WorkerThreadRegular extends Thread {
    private boolean running = true;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (running) {
            System.out.println("Working: " + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    public void stopWorker() {
        running = false;
    }
}

/**
 * WorkerThreadVolatile demonstrates the same thread logic but with a 'volatile' boolean flag.
 * The volatile keyword ensures that updates to the 'running' flag are immediately visible
 * to all threads. This prevents the caching problem seen in WorkerThreadRegular. Like before,
 * we measure the execution time with System.currentTimeMillis().
 */
class WorkerThreadVolatile extends Thread {
    private volatile boolean running = true;

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        while (running) {
            System.out.println("Working: " + Thread.currentThread().getName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " ms");
    }

    public void stopWorker() {
        running = false;
    }
}   