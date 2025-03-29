package synchronization;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
* Synchronised keyword on instance methods only locks the method on the same OBJECT.
* Synchronised keyword on a static method locks the class.
*
* DON'T use
* If performance is critical (consider Lock API for better control).
* If working with read-heavy operations (use ReadWriteLock instead).
* */
public class RaceConditionExample {

    public static void main(String[] args) throws Exception {

        unSyncExample();

        syncExample();

    }

    public static void unSyncExample() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Counter counter = new Counter();
        for(int i=0;i<1000;i++){
            executorService.submit(counter::increment);
        }
        executorService.shutdown();
        executorService.awaitTermination(20, TimeUnit.SECONDS);

        // Result differs each time
        // Incrementing has 3 operations, reading count from memory, adding one, setting count to memory
        // 2 different threads can read the same value from memory at the same time, which causes race condition and misses the update
        System.out.println("Got the result: " + counter.getCount() + " for un-sync counter");
    }

    public static void syncExample() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        SyncCounter syncCounter = new SyncCounter();
        for(int i=0;i<1000;i++){
            executorService.submit(syncCounter::increment);
        }
        executorService.shutdown();
        executorService.awaitTermination(20, TimeUnit.SECONDS);

        System.out.println("Got the result: " + syncCounter.getCount() + " for sync counter");
    }
}

class Counter {
    private int count = 0;

    public void increment() {
        count = count + 1;
    }

    public int getCount() {
        return count;
    }
}

class SyncCounter {
    private int count = 0;

    // Makes it so that only one thread can access this method at a time
    public synchronized void increment() {
        count = count + 1;
    }

    public void increment1() {
        synchronized (this) { // locks 'this' object
            count = count + 1;
        }
    }

    // Ensures only threads using the same lock object are synchronized.
    public void increment2(Object lockedObject) {
        synchronized (lockedObject) { // locks 'this' object
            count = count + 1;
        }
    }

    // Used to synchronize a specific static block instead of the whole method.
    public void increment3() {
        synchronized (SyncCounter.class) {
            count = count + 1;
        }
    }

    public int getCount() {
        return count;
    }
}