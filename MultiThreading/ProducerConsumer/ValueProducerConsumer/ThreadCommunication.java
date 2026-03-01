package MultiThreading.ProducerConsumer.ValueProducerConsumer;

public class ThreadCommunication {
    public static void main(String[] args) throws InterruptedException {
        SharedResource resource = new SharedResource();

        Thread producer  = new Thread(()->{
            for(int i =0;i<10;i++){
                resource.produce(i);
                System.out.println("Produced -"+i);
            }
        });

        Thread consumer = new Thread(()->{
            for(int i =0;i<10;i++){
                System.out.println("Consumed - "+resource.consume());
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }
}
