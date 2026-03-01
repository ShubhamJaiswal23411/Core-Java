package ReentrantLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates the fairness policy of ReentrantLock.
 *
 * When the fairness flag is set to true:
 * - The lock follows FIFO (First-In-First-Out) order.
 * - Threads acquire the lock in the order they requested it.
 * - This reduces the chance of thread starvation.
 *
 * If fairness is false (default), the lock may allow
 * barging (a thread can acquire the lock even if others
 * are waiting), which can improve throughput but may
 * cause starvation.
 */

public class RentrantLockFairnessDemo {

    
    public static void main(String[] args) throws InterruptedException {
        Runnable task = new Runnable() {
            private final Lock lock = new ReentrantLock(true);
            @Override
            public void run(){
                try {
                    lock.lock();
                System.out.println(Thread.currentThread().getName() + " - Running");
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally{
                    lock.unlock();
                }
            }
        };

        List<Thread> threadList = new ArrayList<>();
        for(int i =0;i<10;i++){
            threadList.add(new Thread(task, "Thread-"+i));
        }

        for(Thread t:threadList){
            t.start();
        }

    }
}
