package Executor.CompletableFuture;

import java.util.concurrent.*;

/**
 * Demonstrates usage of CompletableFuture with:
 * - Default ForkJoinPool vs custom executor
 * - thenApply vs thenApplyAsync behavior
 * - thenCompose / thenComposeAsync (flattening)
 * - Thread execution behavior (main vs worker threads)
 * - Combining multiple futures
 *
 * Key Concepts Covered:
 * - Default executor (ForkJoinPool)
 * - Async vs non-async chaining
 * - Thread execution rules
 * - Flattening nested CompletableFutures
 */
public class AsyncMethodsDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // Custom ThreadPoolExecutor with bounded queue and discard policy
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,
                4,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );

        /*
         * If no executor is provided, CompletableFuture uses ForkJoinPool.commonPool()
         */
        CompletableFuture<String> defaultPoolFuture = CompletableFuture.supplyAsync(() -> {
            return "this is the task which uses forkjoinpool";
        });
        System.out.println(defaultPoolFuture.get());

        /*
         * Using a custom executor instead of ForkJoinPool
         */
        CompletableFuture<String> customExecutorFuture = CompletableFuture.supplyAsync(() -> {
            return "this task uses our executor";
        }, executor);
        System.out.println(customExecutorFuture.get());

        /*
         * Non-async chaining (thenApply):
         * - Runs in the thread that completes the previous stage
         * - Often same thread → preserves locality
         * - Ordering is guaranteed due to chaining
         */
        CompletableFuture<String> chainedSync = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 - " + Thread.currentThread().getName());
            return "this is the";
        }, executor).thenApply(s -> {
            System.out.println("Task2 - " + Thread.currentThread().getName());
            return s + " Completable future chaining ";
        });

        System.out.println(chainedSync.get());
        System.out.println("-----");

        /*
         * IMPORTANT:
         * thenApplyAsync:
         * - Always schedules task on a thread pool
         * - Does NOT guarantee different threads, only guarantees pool usage
         * - Execution is asynchronous (non-blocking for caller i.e main thread in this case)
         */
        CompletableFuture<String> asyncChain = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 - " + Thread.currentThread().getName());
            return "this is the Completable future chaining ";
        }).thenApplyAsync(s -> {
            System.out.println("Task2 - " + Thread.currentThread().getName());
            return s + "using thenApplyAsync which uses threadPool ";
        }).thenApplyAsync(s -> {
            System.out.println("Task3 - " + Thread.currentThread().getName());
            return s + "using thenApplyAsync and execution is asynchronous";
        });

        System.out.println(asyncChain.get());

        /*
         * thenCompose:
         * Used when next step returns CompletableFuture
         * Prevents nesting: CompletableFuture<CompletableFuture<T>>
         * - Flattens into CompletableFuture<T>
         */
        CompletableFuture<String> composed = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 - " + Thread.currentThread().getName());
            return "this is the ";
        }).thenCompose(s -> 
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Task2 - " + Thread.currentThread().getName());
                return s + "thenCompose method which ";
            })
        ).thenCompose(s -> 
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Task3 - " + Thread.currentThread().getName());
                return s + "maintains ordering of async tasks";
            })
        );

        System.out.println(composed.get());
        System.out.println("####");

        /*
         * thenComposeAsync:
         * - Introduces async boundary
         * - Each stage may execute in different pools depending on executor provided
         * Flow here:
         * executor → ForkJoinPool → executor
         */
        CompletableFuture<String> composedAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 - " + Thread.currentThread().getName());
            return "this is the ";
        }, executor).thenComposeAsync(s -> 
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Task2 - " + Thread.currentThread().getName());
                return s + "thenCompose method which ";
            })
        ).thenComposeAsync(s -> 
            CompletableFuture.supplyAsync(() -> {
                System.out.println("Task3 - " + Thread.currentThread().getName());
                return s + "maintains ordering of async tasks";
            }, executor)
        );

        System.out.println(composedAsync.get());

        /*
         * VERY IMPORTANT:
         * Non-async methods (thenApply, thenAccept, thenCompose):
         * run in the thread that completes previous task OR the thread waiting (like main calling get()).
         *
         * Behavior:
         * - Task1 completes in worker thread
         * - main calls get() → blocks
         * - Instead of idling, main executes remaining stages
         *
         * Result:
         * Task2 → main
         * Task3 → main
         */
        CompletableFuture<Void> finalStage = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 - " + Thread.currentThread().getName());
            return "This is the first step ";
        }).thenApply(s -> {
            System.out.println("Task2 - " + Thread.currentThread().getName());
            return s + "This is the second step ";
        }).thenAccept(s -> {
            System.out.println("Task3 - " + Thread.currentThread().getName());
            System.out.println(s + "this is the last step");
        });

        System.out.println(finalStage.get());

        /*
         * Combining two independent futures
         */
        CompletableFuture<Integer> intFuture = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<String> stringFuture = CompletableFuture.supplyAsync(() -> "k");

        CompletableFuture<String> combined = intFuture.thenCombine(stringFuture, (i, s) -> i + s);
        System.out.println(combined.get());

        /*
         * Mixed sync + async chain:
         * - thenApply → may run in main (helping thread)
         * - thenApplyAsync → always scheduled to pool
         */
        CompletableFuture<String> mixedChain = CompletableFuture.supplyAsync(() -> {
            System.out.println("Task1 " + Thread.currentThread().getName());
            return "Hello";
        }).thenApply(x -> {
            System.out.println("Task2 " + Thread.currentThread().getName());
            return x + " World";
        }).thenApplyAsync(s -> {
            System.out.println("Task3 " + Thread.currentThread().getName());
            return s + "!!";
        });

        System.out.println(mixedChain.get());

        executor.shutdown();
    }
}
