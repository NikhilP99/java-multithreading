package synchronization;

/*
* In a multithreaded environment, threads may cache variables locally instead of reading from main memory.
* volatile ensures that every read and write happens directly from/to main memory, preventing stale values.
* */
public class MemoryInconsistencyExample {

    public static void main(String[] args) throws InterruptedException {

        // May cause infinite loop and miss updates made by main thread
        NonVolatileSharedResource nonVolatileSharedResource = new NonVolatileSharedResource();
        Thread thread = new Thread(nonVolatileSharedResource.runTask());
        thread.start();

        Thread.sleep(2000);
        nonVolatileSharedResource.running = false;

        Thread.sleep(2000);
        nonVolatileSharedResource.running = true;

        // Will always read updates from memory, no missed updates
        VolatileSharedResource volatileSharedResource = new VolatileSharedResource();
        Thread thread1 = new Thread(volatileSharedResource.runTask());
        thread.start();

        Thread.sleep(2000);
        volatileSharedResource.running = false;

        Thread.sleep(2000);
        volatileSharedResource.running = true;

    }
}

class NonVolatileSharedResource {
    boolean running = true;

    Runnable runTask() {
        return () -> {
            System.out.println("Started working");
            while (running) { // May use cached `running`
                // Do work
            }
            System.out.println("Stopped working");

            while (!running) { // May use cached `running`
                // Wait
            }
            System.out.println("Started working again");
        };
    }
}

class VolatileSharedResource {
    // Will always read and write to memory, no optimisations
    volatile boolean running = true;

    Runnable runTask() {
        return () -> {
            System.out.println("Started working");
            while (running) {
                // Do work
            }
            System.out.println("Stopped working");

            while (!running) {
                // Wait
            }
            System.out.println("Started working again");
        };
    }
}