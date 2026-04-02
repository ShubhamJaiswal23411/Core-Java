package MultiThreading.VirtualThreads;

import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates the performance difference between platform threads
 * and virtual threads when executing an I/O-bound task.
 *
 * This example shows how much difference there is when threads perform
 * an I/O-heavy task (instead of CPU-bound work). In this case,
 * virtual threads significantly reduce thread creation time
 * (more than 20x faster compared to traditional threads).
 */
public class VirtualThreadPerformanceDemo {

    private static final int THREAD_COUNT = 400000;
    private static final String HOST = "www.google.com";
    private static final int PORT = 80;

    /**
     * Simulates an external I/O-bound call by opening a socket connection.
     * This ensures the thread performs blocking I/O instead of CPU work.
     * Though this has a limit as to how many connections you can make so use the other function 
     */
    public static void makeSocketConnectionToGoogle() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to " + HOST);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void performIoTask() {
        try {
            Thread.sleep(100); // simulate blocking I/O (100 ms)
            System.out.println("thread");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // Paltform Threads Creation
        CountDownLatch platformThreadLatch = new CountDownLatch(THREAD_COUNT);

        long platformStartTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                performIoTask(); // Blocking I/O operation
                platformThreadLatch.countDown(); // Signal completion
            }).start();
        }

        // Wait for all platform threads to finish execution
        // platformThreadLatch.await(); // for overall time 
        long platformEndTime = System.currentTimeMillis();
        platformThreadLatch.await(); // just for thread creation time.

        // Virtual Threads Creation
        CountDownLatch virtualThreadLatch = new CountDownLatch(THREAD_COUNT);

        long virtualStartTime = System.currentTimeMillis();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread.startVirtualThread(() -> {
                performIoTask(); // Same blocking I/O operation
                virtualThreadLatch.countDown(); // Signal completion
            });
        }
        // Wait for all virtual threads to finish execution
        // virtualThreadLatch.await(); // for overall time 
        long virtualEndTime = System.currentTimeMillis();
        virtualThreadLatch.await(); //just for thread creation time

        /*
         * -------------------------------
         * 3. Results
         * -------------------------------
         *
         * Note: If you want to see the total time i.e execution + time for thread creation current await position before
         * calculating end time is correct but if you want only the thread creation time then you can move await before 
         * calculating the end time so that only the thread creation time is captured.
         */
        System.out.println(
                "Time taken to create platform threads: "
                        + (platformEndTime - platformStartTime) + " ms");

        System.out.println(
                "Time taken to create virtual threads: "
                        + (virtualEndTime - virtualStartTime) + " ms");
    }
}