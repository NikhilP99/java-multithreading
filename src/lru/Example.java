package lru;

public class Example {
    LRU lru;

    public Example(){
        this.lru = LRU.getInstance();
    }

    public String get(String key){
        return lru.get(key);
    }

    public void put(String key, String value){
        lru.put(key, value);
    }

    public static void main(String[] args) throws InterruptedException {
        Example example = new Example();
        Example example1 = new Example();
        Example example2 = new Example();
        Example example3 = new Example();

        Thread thread1 = new Thread(() -> {
            example1.put("name", "nikhil");
        });
        Thread thread2 = new Thread(() -> {
            example2.put("name", "test");
        });
        Thread thread3 = new Thread(() -> {
            example3.put("age", "25");
        });
        Thread thread4 = new Thread(() -> {
            example.get("name");
        });
        Thread thread5 = new Thread(() -> {
            example.put("age1", "26");
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();

        example.lru.printLru();

    }
}