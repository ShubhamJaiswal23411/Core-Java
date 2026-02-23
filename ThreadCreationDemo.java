/*
 * ThreadCreationDemo
 *
 * SUMMARY:
 * This program demonstrates two different ways to create and run threads in Java:
 *
 * 1) By extending the Thread class
 * 2) By implementing the Runnable interface
 *
 * WHAT THIS PROGRAM SHOWS:
 * - How to create a custom thread by extending Thread.
 * - How to create a task by implementing Runnable.
 *
 * DESIGN DIFFERENCE:
 * - Extending Thread tightly couples the task with the thread itself.
 * - Implementing Runnable separates the task from the thread,
 *   which is generally the preferred and more flexible approach.
 */

public class ThreadCreationDemo {

    public static void main(String[] args) {

        // Creating a thread by extending the Thread class
        CustomThread extendedThread =
                new CustomThread("Thread created using Thread class");

        // Creating a Runnable task
        RunnableTask runnableTask = new RunnableTask();

        /*
         * Runnable does NOT have a start() method.
         * The start() method exists only in the Thread class.
         *
         * Therefore, to execute a Runnable:
         * - We must create a Thread object
         * - Pass the Runnable implementation to the Thread constructor
         */
        Thread runnableBasedThread =
                new Thread(runnableTask, "Thread created using Runnable interface");

        /*
         * Calling start():
         * - Moves the thread to the Runnable state
         * - The thread becomes ready to receive CPU time
         * - The JVM scheduler decides when run() executes
         */
        extendedThread.start();
        runnableBasedThread.start();
    }
}

/*
 * Approach 1: Extending Thread
 *
 * This class represents both:
 * - The task
 * - The thread executing it
 */
class CustomThread extends Thread {

    public CustomThread(String threadName) {
        super(threadName);
    }

    @Override
    public void run() {

        for (int i = 0; i < 20; i++) {
            System.out.println(i + " - " + Thread.currentThread().getName());

            try {
                // Pause execution for 100ms
                // This helps us visually observe thread interleaving
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 * Approach 2: Implementing Runnable
 *
 * This class represents only the task.
 * It does NOT represent a thread.
 * A Thread object is required to execute this task.
 */
class RunnableTask implements Runnable {

    @Override
    public void run() {

        for (int i = 0; i < 20; i++) {
            System.out.println(i + " - " + Thread.currentThread().getName());

            try {
                // Pause execution for 100ms
                // Makes thread switching visible in the output
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}