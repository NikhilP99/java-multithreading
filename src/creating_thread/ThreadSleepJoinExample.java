package creating_thread;

/*
 If a thread holds a lock, it keeps holding the lock during sleep.
*/
public class ThreadSleepJoinExample {

    public static void main(String[] args) {

        Thread thread1 = createThread();
        thread1.setName("Thread1"); // Can rename threads

        Thread thread2 = createThread();
        thread1.setName("Thread2");


        System.out.println("Starting thread 1. Inside : " + Thread.currentThread().getName());
        thread1.start();

        System.out.println("Waiting for thread 1 to complete. Inside : " + Thread.currentThread().getName());
        try {
            thread1.join(2000); // Waiting for 2000 ms, then continue processing
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Waited enough! Starting thread 2. Inside : " + Thread.currentThread().getName());
        thread2.start();

        System.out.println("Finished processing. Inside : " + Thread.currentThread().getName());
    }

    public static Thread createThread(){
        // Directly passing Runnable lambda to Thread constructor
        return new Thread(() -> {
            System.out.println("Entered Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(4000); // Pause the thread for 4000 ms
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Exiting Thread: " + Thread.currentThread().getName());
        });
    }
}
