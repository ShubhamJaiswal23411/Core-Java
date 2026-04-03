package ProducerConsumer.TrueFalseProducerConsumer;

public class Main {
    public static void main(String[] args) {
        SharedResouce resource = new SharedResouce();
        Thread consumer = new Thread(()->{
            while(true){
                resource.consume();
            }
        }, "Consumer Thread");

        Thread producer = new Thread(()->{
            while(true){
                resource.produce();;
            }
        }, "Producer Thread");

        consumer.start();
        producer.start();


    }
}
