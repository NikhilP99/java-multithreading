package creating_thread;

/*
* Extend the thread class
* Implement the run method
* */
class FromThread extends Thread {

    @Override
    public void run(){
        for(int i = 0; i < 5; i++){
            System.out.println("Count: " + i + " Printing Inside : " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000); // Throws checked exception. Must be handled.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Inside : " + Thread.currentThread().getName());

        System.out.println("Creating thread");
        Thread thread = new FromThread();

        System.out.println("Starting thread");
        thread.start();

        // Executes instantly because we are not waiting for the above thread to finish
        System.out.println("Executes immediately. Inside : " + Thread.currentThread().getName());

        // Wait for the thread to finish processing
        thread.join();
        System.out.println("Finished processing. Inside : " + Thread.currentThread().getName());

    }

}


