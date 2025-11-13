package locks;

/*
* To avoid deadlocks:
* use tryLock with timeout
* avoid nested locks - acquiring a lock while holding another lock
* leverage concurrent data structures from java.util.concurrent package
* */
public class DeadlockExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Inside thread : " + Thread.currentThread().getName());

        String resource1 = "Resource 1";
        String resource2 = "Resource 2";

        Thread thread1 = new Thread(() -> {
            synchronized (resource1) {
                System.out.println("Acquired lock for resource 1 from thread : " + Thread.currentThread().getName());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (resource2) {
                    System.out.println("Acquired lock for resource 2 from thread : " + Thread.currentThread().getName());
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (resource2) {
                System.out.println("Acquired lock for resource 2 from thread : " + Thread.currentThread().getName());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (resource1) {
                    System.out.println("Acquired lock for resource 1 from thread : " + Thread.currentThread().getName());
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("This statement will never print because of deadlock");
    }
}
