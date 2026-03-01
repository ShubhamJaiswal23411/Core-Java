package MultiThreading.DeadLock;
public class DeadLock {
    public static void main(String[] args) throws InterruptedException {
        PaperWithDeadlock paper = new PaperWithDeadlock();
        PenWithDeadlock pen = new PenWithDeadlock();
        Thread t1 = new Thread(() -> {
            paper.havePaperNeedPen(pen);
        });
        Thread t2 = new Thread(() -> {
            pen.havePenNeedPaper(paper);
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}

class PenWithDeadlock {

    public synchronized void havePenNeedPaper(PaperWithDeadlock paper) {
        log(" - I have the lock for Pen Now Trying to acquire the paper lock");
        paper.getPaper();
    }

    public synchronized void getPen() {
        log(" - providing pen");
    }

    private void log(String s) {
        System.out.println(Thread.currentThread().getName() + s);
    }

}

class PaperWithDeadlock {

    public synchronized void havePaperNeedPen(PenWithDeadlock pen) {
        log(" - I have the lock for Paper Now Trying to acquire the pen lock");
        pen.getPen();
    }

    public synchronized void getPaper() {
        log(" - providing pen");
    }

    private void log(String s) {
        System.out.println(Thread.currentThread().getName() + s);
    }

}