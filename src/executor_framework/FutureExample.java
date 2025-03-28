package executor_framework;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class FutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Creating executor service. Inside : " + Thread.currentThread().getName());
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        basicFutureExample(executorService);

        timedOutFutureExample(executorService);

        isDoneFutureExample(executorService);

        finishAllFuturesExample(executorService);

        finishAnyFutureExample(executorService);

        // TODO: Add cancel future example

        executorService.shutdown();
    }

    public static void basicFutureExample(ExecutorService executorService) throws ExecutionException, InterruptedException {
        System.out.println("Submitting callable to executor. Inside : " + Thread.currentThread().getName());
        Future<String> future = executorService.submit(getCallable());

        System.out.println("This line executes immediately. Waiting for future result. Inside : " + Thread.currentThread().getName());
        String result = future.get();
        System.out.println("Got the result from future - " + result + " Inside : " + Thread.currentThread().getName());
    }

    public static void timedOutFutureExample(ExecutorService executorService) throws ExecutionException, InterruptedException {
        // Add timeout on the future
        Future<String> timedOutFuture = executorService.submit(getCallable());

        try {
            String timedOutResult = timedOutFuture.get(2, TimeUnit.SECONDS);
        } catch (TimeoutException ex){
            System.out.println("Future timed out. Took more than 2 seconds. Inside : " + Thread.currentThread().getName());
        }
    }

    public static void isDoneFutureExample(ExecutorService executorService) throws InterruptedException, ExecutionException {
        // Can check if future is finished manually
        Future<String> future1 = executorService.submit(getCallable());
        while(!future1.isDone()) {
            System.out.println("Future task is still not done...");
            Thread.sleep(200);
        }
        String result1 = future1.get();
        System.out.println("Future task finished. Got result " + result1 + " . Inside : " + Thread.currentThread().getName());

    }

    public static void finishAllFuturesExample(ExecutorService executorService) throws ExecutionException, InterruptedException {
        // Finish when ALL the callables are finished
        List<Future<String>> futures = executorService.invokeAll(Arrays.asList(getCallable(), getCallable(), getCallable()));

        for(Future<String> ft: futures) {
            // The result is printed only after all the futures are complete. (i.e. after 10 seconds since there are 3 tasks and 2 threads in the pool)
            System.out.println("Future task finished. Got result " + ft.get() + " . Inside : " + Thread.currentThread().getName()); // We can call get() on any of the included futures
        }
    }

    public static void finishAnyFutureExample(ExecutorService executorService) throws ExecutionException, InterruptedException {
        // WAIT for any of the tasks to finish. This doesn't return a future but blocks until any of them returns and discards the rest of the results.
        String fastestResult = executorService.invokeAny(Arrays.asList(getCallable(), getCallable(), getCallable()));
        System.out.println("Future task finished. Got result " + fastestResult + " . Inside : " + Thread.currentThread().getName());
    }


    public static Callable<String> getCallable(){
        return () -> {
            System.out.println("Printing Inside : " + Thread.currentThread().getName());
            Thread.sleep(5000);
            return Thread.currentThread().getName();
        };
    }
}
