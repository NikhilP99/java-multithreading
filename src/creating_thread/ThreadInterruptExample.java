package creating_thread;

/*
* Interrupting a thread doesn't stop the thread, it just updates an internal flag
* If a thread is interrupted during sleep, it will throw error
* */
public class ThreadInterruptExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Inside : " + Thread.currentThread().getName());

        Thread thread = new Thread(getRunnable());
        thread.start();

        System.out.println("Waiting 2 seconds before interrupting a thread : " + thread.getName());
        Thread.sleep(2000);
        thread.interrupt();

        Runnable workingUntilInterrupted = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Thread is running: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Thread: " + Thread.currentThread().getName() + " got interrupted during sleep!");
                    System.out.println("Is interrupted: " + Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt(); // Re-interrupt the thread
                }
            }
            System.out.println("Thread is exiting: " + Thread.currentThread().getName());
        };

        Thread newThread = new Thread(workingUntilInterrupted, "workingUntilInterrupted");
        newThread.start();
        Thread.sleep(3000);
        newThread.interrupt();

    }

    public static Runnable getRunnable(){
        return () -> {
            System.out.println("Printing Inside : " + Thread.currentThread().getName());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) { // Throws exception only if interrupted during sleep
                System.out.println("Thread: " + Thread.currentThread().getName() + " got interrupted!");
                // break or return here if you need to stop thread processing on interrupt
            }
            System.out.println("Thread finished processing. Printing Inside : " + Thread.currentThread().getName());
        };
    }

}
