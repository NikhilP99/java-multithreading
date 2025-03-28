package executor_framework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
* Executor framework handles the following:
* Thread creation
* Managing lifecycle of threads
* Submission/scheduling of tasks and executing them
* */
public class ExecutorServiceExample {

    public static void main(String[] args) {
        fixedThreadPoolExample();

        scheduledThreadPoolExample();
    }

    public static void fixedThreadPoolExample(){
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Directly pass runnable to the ExecutorService, thread creation will be handled internally
        executorService.submit(getRunnable());
        executorService.submit(getRunnable());

        // Extra tasks are put in blocking queue, they get executed when current threads finish
        executorService.submit(getRunnable());
        executorService.submit(getRunnable());

        // The program will not exit if executor service is not shut down manually
        executorService.shutdown(); // completes the current tasks and shutdown

        // terminates every running tasks and shut down
//        executorService.shutdownNow();
    }

    public static void scheduledThreadPoolExample(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        // Runs after a delay of 5 seconds
        System.out.println("Submitting task at " + System.nanoTime() + " to be executed after 5 seconds.");
        scheduledExecutorService.schedule(getRunnable(), 5, TimeUnit.SECONDS);

        // Keeps running at an interval of 2 seconds after a delay of 5 seconds
        System.out.println("Submitting task at " + System.nanoTime() + " to be executed every 2 seconds after 5 seconds initial delay.");
        scheduledExecutorService.scheduleAtFixedRate(getRunnable(), 5, 2, TimeUnit.SECONDS);

    }


    public static Runnable getRunnable(){
        return () -> {
            System.out.println("Printing Inside : " + Thread.currentThread().getName());
            for (int i = 0; i < 5; i++) {
                System.out.println("Count: " + i + " Printing Inside : " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

}
