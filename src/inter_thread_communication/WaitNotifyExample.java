package inter_thread_communication;

import java.util.LinkedList;
import java.util.Queue;

/*
* These methods can only be used from inside a synchronised block
* wait() puts the current thread into a separate wait set and releases the lock
* notify() awakens a random thread from wait set, notifyAll() awakens all threads and, they compete for the lock
* */
public class WaitNotifyExample {

    private Queue<Integer> queue;
    private final Integer capacity;

    public WaitNotifyExample(Integer capacity){
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    private synchronized void produce(Integer num) throws InterruptedException {
        // Always check wait condition in a loop in case any thread wakes up without notify
        while(this.queue.size() >= capacity){
            wait();
        }

        queue.add(num);
        System.out.println("Produced " + num + " from thread " + Thread.currentThread().getName());

        // Notify other waiting threads
        notifyAll(); // when in doubt, notifyAll() instead of notify()

        // This function does not release lock immediately after notify
    }

    private synchronized void consume() throws InterruptedException {
        // Always check wait condition in a loop in case any thread wakes up without notify
        while(this.queue.isEmpty()){
            wait();
        }

        Integer consumed = queue.poll();
        System.out.println("Consumed " + consumed + " from thread " + Thread.currentThread().getName());

        // Notify other waiting threads
        notifyAll(); // when in doubt, notifyAll() instead of notify()

        // This function does not release lock immediately after notify
    }

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyExample example = new WaitNotifyExample(3);

        Thread producerThread = new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    example.produce(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Thread consumerThread = new Thread(() -> {
            for(int i=0;i<10;i++) {
                try {
                    example.consume();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.join();
    }

}
