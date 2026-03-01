package MultiThreading.ProducerConsumer;

public class SharedResouce {

    private boolean resource = false;

    public synchronized void produce() {
        if (!resource) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource = true;
            log(" - produced item");
        }
        notifyAndWait();
    }

    public synchronized void consume() {
        if (resource) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource = false;
            log(" - consumed item");
        } else {
            log(" - There is no item to consume , waiting for the producer to produce");
        }
        notifyAndWait();
    }

    public void notifyAndWait() {
        try {
            notify();
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String s) {
        System.out.println(Thread.currentThread().getName() + s);
    }

}
