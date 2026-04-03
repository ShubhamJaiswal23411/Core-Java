package ProducerConsumer.TrueFalseProducerConsumer;

public class SharedResouce {

    private boolean resource = false;

    public synchronized void produce() {
        while (!resource) {
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
        while (resource) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource = false;
            log(" - consumed item");
        }
        notifyAndWait();
    }

    public void notifyAndWait() {
        try {
            notifyAll();
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void log(String s) {
        System.out.println(Thread.currentThread().getName() + s);
    }

}
