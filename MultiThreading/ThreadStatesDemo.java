package MultiThreading;
public class ThreadStatesDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getState());//Runnable
            System.out.println("RUNNING");//Running
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        },"First Thread");

        System.out.println(t1.getState());//New
        t1.start();
        Thread.sleep(100);
        System.out.println(t1.getState());//TIME_WAITING
        ThreadStatesDemo object = new ThreadStatesDemo();
        Thread t2 = new Thread(()->{
            synchronized(object){
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        t2.start();
        Thread.sleep(200);//so that t2 aquires lock on object and is in waiting state
        System.out.println(t2.getState());//WAITING

        synchronized(object){
            object.notify();//Notifying the t2 state
        }

        Thread t3 = new Thread(()->{
            synchronized(object){
                System.out.println("I have finally came out of blocked state and have aquired lock on the object");
            }
        });
        
        synchronized(object){
            t3.start();
            Thread.sleep(200);//sleeping main thread doesnt releases the lock and makes sure that within that time 
            // frame t3 must have tried to acquire lock and went to the blocked state.
            System.out.println(t3.getState());//Blocked
        }
        t1.join();
        t2.join();
        t3.join();
        System.out.println(t1.getState());
    }
}
