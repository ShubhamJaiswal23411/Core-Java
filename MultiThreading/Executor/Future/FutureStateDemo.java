package Executor.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Demonstrates usage of Future with ThreadPoolExecutor.
 *
 * Key points covered:
 * 1. Shows different states of a Future: RUNNING, SUCCESS, FAILED, CANCELLED.
 * 2. Demonstrates commonly used Future methods like isDone(), isCancelled(), get(), cancel(), and state().
 * 3. Uses Future<?> since Runnable does not return a value (its return type is void).
 */
public class FutureStateDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Custom thread pool configuration
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,                           // core pool size
                4,                        // max pool size
                100,                        // keep alive time
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3),// task queue
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        // -------- SUCCESS CASE --------
        Future<?> successFuture = submitTask(threadPool, false);

        System.out.println(successFuture.isDone()); // likely false initially

        Thread.sleep(2000);

        System.out.println(successFuture.isCancelled()); // false
        System.out.println(successFuture.state());       // RUNNING

        // get() blocks until completion; returns null for Runnable
        System.out.println(successFuture.get());

        System.out.println(successFuture.isDone()); // true
        System.out.println(successFuture.state());  // SUCCESS


        // -------- CANCELLATION CASE --------
        Future<?> cancelledFuture = submitTask(threadPool, false);

        // Attempt to cancel the task (interrupt if running)
        System.out.println(cancelledFuture.cancel(true));

        System.out.println(cancelledFuture.isCancelled()); // true
        System.out.println(cancelledFuture.isDone());      // true (done includes cancelled/failed)
        System.out.println(cancelledFuture.state());       // CANCELLED


        // -------- FAILURE CASE --------
        List<Future<?>> futures = new ArrayList<>();

        try {
            Future<?> failedFuture = submitTask(threadPool, true);
            futures.add(failedFuture);

            Thread.sleep(1000); // give time for execution
        } catch (Exception ignored) {
            // Ignored for demonstration
        }

        System.out.println(futures.get(0).state()); // FAILED
    }

    /**
     * Submits a task to the executor.
     *
     * @param threadPool     executor service used to run the task
     * @param shouldFail     if true, task throws RuntimeException to simulate failure
     * @return Future representing the submitted task
     */
    private static Future<?> submitTask(ThreadPoolExecutor threadPool, boolean shouldFail) {

        return threadPool.submit(() -> {
            try {
                // Simulate failure scenario
                if (shouldFail) {
                    throw new RuntimeException("Simulated task failure");
                }
                // Simulate long-running task
                Thread.sleep(3000);
                System.out.println("Task executed by thread: "
                        + Thread.currentThread().getName());

            } catch (InterruptedException e) {
                // Restore interrupt status after catching InterruptedException
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
    }
}