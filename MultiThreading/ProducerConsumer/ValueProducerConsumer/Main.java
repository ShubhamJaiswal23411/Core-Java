package ProducerConsumer.ValueProducerConsumer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        SharedResource resource = new SharedResource();

        Thread producer  = new Thread(()->{
            for(int i =1;i<=10;i++){
                resource.produce(i);
            }
        });

        Thread consumer = new Thread(()->{
            for(int i =0;i<10;i++){
                resource.consume();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();

    }
}
