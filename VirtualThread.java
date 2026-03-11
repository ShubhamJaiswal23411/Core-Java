import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import jdk.management.VirtualThreadSchedulerMXBean;

public class VirtualThread implements Serializable, Cloneable {
    public static void main(String[] args) throws InterruptedException {
        
        System.out.println(Runtime.getRuntime().availableProcessors());
        VirtualThreadSchedulerMXBean mXBean = ManagementFactory.getPlatformMXBean(
                        VirtualThreadSchedulerMXBean.class);

        System.out.println(mXBean);//[parallelism=16, size=0, mounted=0, queued=0]        
        int threadCount = 100;
        Runnable task = ()->{
            System.out.println("Thread Started "+ Thread.currentThread());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Thread Ended "+Thread.currentThread());
        };

        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<threadCount;i++){
            threads.add(Thread.ofVirtual().unstarted(task));
        }
        for(Thread t : threads){
            t.start(); 
        }
        for(Thread t : threads){
            t.join();
        }
        System.out.println(mXBean);//[parallelism=16, size=16, mounted=0, queued=0]

    }


}
