package Executor.ThreadPoolExecutor;

public class CustomRejectionException extends RuntimeException{

    public CustomRejectionException() {
    }

    public CustomRejectionException(String message) {
        super(message);
    }
   
}
