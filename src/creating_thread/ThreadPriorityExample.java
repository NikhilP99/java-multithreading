package creating_thread;

/*
* Thread priority is more of a hint than a rule
* It does not guarantee the intended behavior
* */
public class ThreadPriorityExample {

    public static void main(String[] args) {

        System.out.println("Max priority - " + Thread.MAX_PRIORITY);
        System.out.println("Min priority - " + Thread.MIN_PRIORITY);

        Thread thread1 = new Thread(ThreadPriorityExample::process, "Thread1");
        Thread thread2 = new Thread(ThreadPriorityExample::process, "Thread2");
        Thread thread3 = new Thread(ThreadPriorityExample::process, "Thread3");

        thread1.setPriority(Thread.MAX_PRIORITY);
        thread2.setPriority(Thread.NORM_PRIORITY);
        thread3.setPriority(Thread.MIN_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public static void process(){
        System.out.println("Printing Inside : " + Thread.currentThread().getName());
        for(int i = 0; i < 5; i++){
            System.out.println("Count: " + i + " Printing Inside : " + Thread.currentThread().getName() + " with priority - " + Thread.currentThread().getPriority());
        }
    }
}
