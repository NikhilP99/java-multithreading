package inter_thread_communication;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
* Better for performance than synchronised block
* await() puts the current thread into a separate wait set and releases the lock
* signal() awakens a random thread from wait set, signalAll() awakens all threads, and they compete for the lock
*
* Provides more control since we can create multiple conditions on the same lock (multiple wait sets based on condition)
* Better variants like await(long time, TimeUnit unit), awaitUninterruptibly()
* */
public class ConditionLockExample {

    private Queue<Integer> queue;
    private final Integer capacity;
    private final Lock lock;
    private final Condition condition;

    public ConditionLockExample(Integer capacity){
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
    }

    private void produce(Integer num) throws InterruptedException {
        System.out.println("Acquiring lock from thread: " + Thread.currentThread().getName());
        lock.lock();
        try {
            // Always check await condition in a loop in case any thread wakes up without notify
            while(this.queue.size() >= capacity){
                condition.await();
            }

            queue.add(num);
            System.out.println("Produced " + num + " from thread " + Thread.currentThread().getName());

            // Notify other waiting threads
            condition.signalAll(); // when in doubt, signalAll() instead of signal()

            // This function does not release lock immediately after signal
        } finally {
            System.out.println("Releasing lock from thread: " + Thread.currentThread().getName());
            lock.unlock();
        }

    }

    private void consume() throws InterruptedException {
        System.out.println("Acquiring lock from thread: " + Thread.currentThread().getName());
        lock.lock();
        try {
            // Always check await condition in a loop in case any thread wakes up without notify
            while(this.queue.isEmpty()){
                condition.await();
            }

            Integer consumed = queue.poll();
            System.out.println("Consumed " + consumed + " from thread " + Thread.currentThread().getName());

            // Notify other waiting threads
            condition.signalAll(); // when in doubt, signalAll() instead of signal()

            // This function does not release lock immediately after signal
        } finally {
            System.out.println("Releasing lock from thread: " + Thread.currentThread().getName());
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionLockExample example = new ConditionLockExample(3);

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
