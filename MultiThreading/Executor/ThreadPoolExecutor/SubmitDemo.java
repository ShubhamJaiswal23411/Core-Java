package Executor.ThreadPoolExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Demonstrates different variants of the submit() method in ThreadPoolExecutor.
 *
 * Key points:
 * 1. submit(Runnable) → returns Future<?> (result is always null if task completes successfully)
 * 2. submit(Runnable, T result) → returns Future<T> (returns the provided result after execution)
 * 3. submit(Callable<T>) → returns Future<T> (returns the value produced by call())
 *
 * Also demonstrates:
 * - Behavior of thread pool lifecycle
 * - Importance of shutting down executor or allowing core threads to time out
 */
public class SubmitDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Creating a ThreadPoolExecutor with:
        // corePoolSize = 2, maxPoolSize = 4
        // keepAliveTime = 100 ms
        // queue size = 3
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                4,
                100,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        // Allows even core threads to terminate after being idle
        executor.allowCoreThreadTimeOut(true);

        // --- 1. submit(Runnable) ---
        Future<?> runnableFuture = executor.submit(
                () -> System.out.println("Runnable task executed")
        );

        try {
            // For Runnable, get() always returns null on successful execution
            Object result = runnableFuture.get();
            System.out.println("Runnable result is null: " + (result == null));
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.toString());
        }

        // --- 2. submit(Runnable, T result) ---
        List<Integer> sharedList = new ArrayList<>();
        ListModifierTask task = new ListModifierTask(sharedList);

        // The returned Future will return the provided sharedList reference
        Future<List<Integer>> runnableWithResultFuture = executor.submit(task, sharedList);

        List<Integer> modifiedList = runnableWithResultFuture.get();
        System.out.println("Modified list: " + modifiedList);

        // --- 3. submit(Callable<T>) ---
        Future<Integer> callableFuture = executor.submit(() -> {
            System.out.println("Callable task executed");
            return 100;
        });

        Integer callableResult = callableFuture.get();
        System.out.println("Callable result: " + callableResult);

        // Give time for threads to become idle and terminate (since timeout is enabled)
        Thread.sleep(2000);

        // Submitting a task again after threads may have terminated
        executor.submit(() ->
                System.out.println("Task submitted after idle timeout; executor creates threads if needed")
        );

        /*
         * Important Notes:
         *
         * - ThreadPoolExecutor does NOT shut down automatically.
         * - Even if threads time out (via allowCoreThreadTimeOut), the executor itself remains active.
         * - You should ALWAYS call executor.shutdown() (or shutdownNow()) to properly release resources.
         *
         * - keepAliveTime only applies to non-core threads by default.
         *   To apply it to core threads, allowCoreThreadTimeOut(true) must be enabled.
         *
         * - If you don't shut down the executor:
         *   → It may prevent JVM termination (depending on thread state)
         *   → It can lead to resource leaks
         *
         * - Even after all threads die, submitting a new task will recreate threads.
         */

        executor.shutdown(); // Proper cleanup
    }
}

/**
 * A simple Runnable task that modifies a shared list.
 *
 * Adds a fixed value (2) to the provided list when executed.
 * Demonstrates how submit(Runnable, T) can return a pre-defined result object.
 */
class ListModifierTask implements Runnable {

    private final List<Integer> list;

    public ListModifierTask(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        // Critical section: modifying shared mutable state
        // In real-world scenarios, consider thread-safety (e.g., synchronized list)
        list.add(2);
    }
}