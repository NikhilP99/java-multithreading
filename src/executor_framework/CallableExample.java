package executor_framework;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/*
* Callable can return a value unlike a Runnable
* */
public class CallableExample {

    public static void main(String[] args) throws Exception {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating Callable");
        Callable<String> callableFromImplementingAnInterface = new CallableExample.ExampleCallable();

        Callable<String> callableFromAnonClass = new Callable<>() {
            @Override
            public String call() throws InterruptedException {
                System.out.println("Implementing Callable from anon class. Printing Inside : " + Thread.currentThread().getName());
                return process();
            }
        };

        // Lambdas can only be created for SAM (single abstract method) interfaces
        Callable<String> callableFromLambda = () -> {
            System.out.println("Implementing Callable from lambda. Printing Inside : " + Thread.currentThread().getName());
            return process();
        };


        // To run a Callable without using Executor Framework, you need to create FutureTask
        System.out.println("Creating FutureTask. Printing Inside : " + Thread.currentThread().getName());
        FutureTask<String> futureTask = new FutureTask<>(callableFromLambda);

        System.out.println("Creating and running new thread. Printing Inside : " + Thread.currentThread().getName());
        Thread thread = new Thread(futureTask);
        thread.start();

        // Get the result (blocks until done)
        String result = futureTask.get();
        System.out.println("Result from future task: " + result);

        // We can directly execute callable but it executes in the main thread, defeating the purpose
        String resultFromDirectCallable = callableFromAnonClass.call();
        System.out.println("Result from direct callable: " + resultFromDirectCallable);

    }


    static class ExampleCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Implementing Callable interface example. Printing Inside : " + Thread.currentThread().getName());
            return process();
        }
    }

    public static String process() throws InterruptedException {
        System.out.println("Printing Inside : " + Thread.currentThread().getName());
        Thread.sleep(5000); // Callable can throw a checked exception, no need to handle it here
        return Thread.currentThread().getName();
    }
}
