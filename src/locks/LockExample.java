package locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockExample {
    public static void main(String[] args) {
        System.out.println("Inside thread : " + Thread.currentThread().getName());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        ReentrantLockExample reentrantLockExample = new ReentrantLockExample();

        executorService.submit(() -> System.out.println("IncrementCount : " + reentrantLockExample.incrementAndGet() + " from thread: " + Thread.currentThread().getName()));
        executorService.submit(() -> System.out.println("IncrementCount : " + reentrantLockExample.incrementAndGet() + " from thread: " + Thread.currentThread().getName()));

        executorService.shutdown();
    }
}

/*
* Same behavior as the intrinsic/implicit lock accessed via the synchronized keyword.
* */
class ReentrantLockExample {
    private final ReentrantLock lock = new ReentrantLock();

    private int count = 0;

    // Simple lock acquiring example
    public void increment(){
        System.out.println("Acquiring Reentrant lock for thread : " + Thread.currentThread().getName());
        lock.lock();

        try {
            count = count + 1;
        } finally {
            System.out.println("Releasing Reentrant lock from thread : " + Thread.currentThread().getName());
            lock.unlock();
        }
    }

    // More fine-grained control of locks
    public int incrementAndGet(){ 
        System.out.println("Is it locked : " + lock.isLocked());

        System.out.println("Is lock held by current thread : " + lock.isHeldByCurrentThread());

        // tries to acquire the lock without pausing the thread
        // if fails, it returns immediately instead of waiting for the lock to be released
        boolean isAcquired = lock.tryLock(); // can also provide timeout if want to wait
        System.out.println("Lock Acquired : " + isAcquired + " by thread: " + Thread.currentThread().getName());

        if(isAcquired) {
            try {
                Thread.sleep(2000);
                count = count + 1;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            } finally {
                lock.unlock();
            }
        }
        return count;
    }

}

/*
* ReadWriteLock allows for an increased level of concurrency. It performs better compared to other locks in applications where there are fewer writes than reads.
* The read lock may be held by multiple threads simultaneously as long as the write lock is not held by any thread
* */
class ReadWriteCounter {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private int count = 0;

    public int incrementAndGet(){
        System.out.println("Acquiring write lock for thread : " + Thread.currentThread().getName());
        lock.writeLock().lock();
        try {
            count = count + 1;
            return count;
        } finally {
            lock.writeLock().unlock();
            System.out.println("Releasing write lock from thread : " + Thread.currentThread().getName());
        }
    }

    public int getCount(){
        System.out.println("Acquiring read lock for thread : " + Thread.currentThread().getName());
        lock.readLock().lock();
        try {
           return count;
        } finally {
            lock.readLock().unlock();
            System.out.println("Releasing read lock from thread : " + Thread.currentThread().getName());
        }
    }
}