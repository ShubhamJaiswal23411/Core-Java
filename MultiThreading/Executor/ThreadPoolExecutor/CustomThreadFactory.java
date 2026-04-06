package Executor.ThreadPoolExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory{
    private final AtomicInteger currentThread = new AtomicInteger(1);

    @Override
    public Thread newThread(Runnable r) {
        Thread curThread = new Thread(r);
        curThread.setName("MyThread - " + currentThread.getAndIncrement());
        curThread.setPriority(Thread.MAX_PRIORITY);
        return curThread;
    }

}