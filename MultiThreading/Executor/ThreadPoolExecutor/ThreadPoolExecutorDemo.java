package Executor.ThreadPoolExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates ThreadPoolExecutor behavior:
 * - Task submission & execution
 * - Queue saturation and rejection
 * - Core thread timeout (thread dying)
 * - Custom ThreadFactory & RejectionHandler usage
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) throws InterruptedException {

        log("Starting default executor demo...");
        runDefaultExecutorDemo();

        log("\nStarting custom executor demo...");
        runCustomExecutorDemo();
    }

    /**
     * Demonstrates default ThreadPoolExecutor behavior
     */
    private static void runDefaultExecutorDemo() throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                100,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3)
        );

        executor.allowCoreThreadTimeOut(true);

        try {
            Runnable task = createTask();
            submitTasks(executor, task, 10, 1100);
            observeThreadPool(executor);
        } finally {
            shutdownExecutor(executor, 20);
        }
    }

    /**
     * Demonstrates custom ThreadFactory and RejectionHandler
     */
    private static void runCustomExecutorDemo() throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                100,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3),
                new CustomThreadFactory(),
                new CustomRejectionExecutionHandler()
        );
        executor.allowCoreThreadTimeOut(true);
        try {
            Runnable task = createTask();
            submitTasks(executor, task, 10, 1000);
            observeThreadPool(executor);
        } finally {
            shutdownExecutor(executor, 20);
        }
    }

    /**
     * Creates a reusable task
     */
    private static Runnable createTask() {
        return () -> {
            try {
                Thread.sleep(5000); // Simulate work
                log("Task completed");
            } catch (InterruptedException e) {
                log("Task interrupted");
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Submits tasks and logs executor stats
     */
    private static void submitTasks(ThreadPoolExecutor executor, Runnable task,
                                    int count, int delayMs) throws InterruptedException {

        for (int i = 0; i < count; i++) {
            try {
                executor.submit(task);
                log("Task submitted: " + i);
            } catch (Exception e) {
                // Happens when queue + max threads are full
                log("Task rejected: " + e);
            }

            Thread.sleep(delayMs);

            // Executor state monitoring
            log("Total tasks: " + executor.getTaskCount());
            log("Active tasks: " + executor.getActiveCount());
            log("Completed tasks: " + executor.getCompletedTaskCount());
        }
    }

    /**
     * Observes thread pool shrinking due to idle timeout
     */
    private static void observeThreadPool(ThreadPoolExecutor executor)
            throws InterruptedException {

        log("Observing thread death (idle timeout)...");

        for (int i = 0; i < 10; i++) {
            Thread.sleep(700);
            log("Current pool size: " + executor.getPoolSize());
        }
    }

    /**
     * Gracefully shuts down executor
     */
    private static void shutdownExecutor(ThreadPoolExecutor executor, int timeoutSeconds)
            throws InterruptedException {

        log("Shutting down executor...");
        executor.shutdown();

        if (!executor.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
            log("Forcing shutdown...");
            executor.shutdownNow();
        }

        log("Executor terminated.");
    }

    /**
     * Central logging method
     * Prints thread name + message
     */
    private static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
    }
}