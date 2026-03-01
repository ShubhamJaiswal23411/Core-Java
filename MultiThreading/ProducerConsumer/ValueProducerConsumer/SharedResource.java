package MultiThreading.ProducerConsumer.ValueProducerConsumer;

public class SharedResource {
    private int data = 0;
    private boolean hasData = false;

    public synchronized void produce(int val) {
        if (hasData) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        data = val;
        hasData = true;
        notify();
    }

    public synchronized int consume(){
        while(!hasData){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        hasData = false;
        notify();
        return data;
    }

}
