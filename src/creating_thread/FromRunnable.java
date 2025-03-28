package creating_thread;

/*
 * Implement the Runnable interface
 * More flexible than extending Thread class
 * Because you can implement multiple interfaces but only extend one class
 * */
public class FromRunnable {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Inside : " + Thread.currentThread().getName());

        // Any thread created with Runnable doesn't return anything
        System.out.println("Creating Runnable");
        Runnable runnableFromImplementingAnInterface = new ExampleRunnable();

        Runnable runnableFromAnonClass = new Runnable() {
            @Override
            public void run() {
                System.out.println("Implementing Runnable from anon class. Printing Inside : " + Thread.currentThread().getName());
                process();
            }
        };

        // Lambdas can only be created for SAM (single abstract method) interfaces
        Runnable runnableFromLambda = () -> {
            System.out.println("Implementing Runnable from lambda. Printing Inside : " + Thread.currentThread().getName());
            process();
        };

        // Pass runnable to thread constructor
        Thread thread1 = new Thread(runnableFromImplementingAnInterface);
        Thread thread2 = new Thread(runnableFromAnonClass);
        Thread thread3 = new Thread(runnableFromLambda);

        System.out.println("Starting threads");
        thread1.start();
        thread2.start();
        thread3.start();

        // Executes instantly because we are not waiting for the above thread to finish
        System.out.println("Executes immediately. Inside : " + Thread.currentThread().getName());

        // Wait for the thread to finish processing
        thread1.join();
        thread2.join();
        thread3.join();
        System.out.println("Finished processing. Inside : " + Thread.currentThread().getName());

    }

    static class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("Implementing Runnable example. Printing Inside : " + Thread.currentThread().getName());
            process();
        }
    }


    public static void process(){
        System.out.println("Printing Inside : " + Thread.currentThread().getName());
        for(int i = 0; i < 5; i++){
            System.out.println("Count: " + i + " Printing Inside : " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // Throws checked exception. Must be handled.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
