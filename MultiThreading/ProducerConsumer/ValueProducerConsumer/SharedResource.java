package MultiThreading.ProducerConsumer.ValueProducerConsumer;
/*
    Important point : this producer consumer problem works with an if condition as wel without a while
    but the reason for using a while is that a thread might wake if someone else called a notify all as well 
    in that case it is gonna start its execution instead of checking if the flag is true or false
    that will break the code 
    so thats why we should use while loop instead of an if.

*/


public class SharedResource {
    private int data = 0;
    private boolean hasData = true;

    public synchronized void produce(int val) {
        while (hasData) {
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
        System.out.println("Produced -" + val);
        notifyAll();
    }

    public synchronized void consume() {
        while (!hasData) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        hasData = false;
        System.out.println("Consumed - "+ data);
        notifyAll();
        
    }

}
