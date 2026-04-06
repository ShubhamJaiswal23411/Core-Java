package Executor.ThreadPoolExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class CustomRejectionExecutionHandler implements RejectedExecutionHandler{

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        throw new CustomRejectionException("Queue is full and max core pool size has been reached, here is the current Thread pool status : "+ executor.toString());
    }

    
}
