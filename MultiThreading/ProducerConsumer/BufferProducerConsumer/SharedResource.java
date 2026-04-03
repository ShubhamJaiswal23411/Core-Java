package ProducerConsumer.BufferProducerConsumer;

import java.util.ArrayList;
import java.util.List;

public class SharedResource {

    List<Integer> buffer = new ArrayList<>();
    private final int threshold = 5;

    public synchronized void produce(int i) {

        while (buffer.size() == threshold) {
            System.out.println("Buffer is full so producer thread is wating");
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        buffer.add(i);
        System.out.println("Produced - " + i);
        notifyAll();

    }

    public synchronized void consume() {
        while (buffer.size() <= 0) {
            System.out.println("Buffer is empty so wating for producer thread to produce items");
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        while (buffer.size() > 0) {
            System.out.println("Consuming Item - " + buffer.get(buffer.size() - 1));
            buffer.remove(buffer.size() - 1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        notifyAll();

    }

}
