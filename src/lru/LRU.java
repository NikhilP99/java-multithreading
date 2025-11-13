package lru;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LRU extends LinkedHashMap <String, String> {

    private static Integer capacity;

    public static LRU INSTANCE = null;

    private LRU(Integer initialCapacity, Float loadFactor, Boolean accessOrder){
        super(initialCapacity, loadFactor, accessOrder);
        capacity = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, String> eldest){
        return this.size() > capacity;
    }

    @Override
    public synchronized String get(Object key) {
        System.out.println("Getting key "  + key + " from thread " + Thread.currentThread().getName());

        if(this.containsKey(key)){
            return super.get(key);
        }
        return null;
    }

    @Override
    public synchronized String put(String key, String value) {
        System.out.println("Putting " + value + " key from thread " + Thread.currentThread().getName());
        this.remove(key);
        super.put(key, value);
        return value;
    }

    public void printLru(){
        List<String> toPrint = new ArrayList<>();
        for(Map.Entry<String, String> entry: this.entrySet()){
            toPrint.add(entry.getKey() + " : " + entry.getValue());
        }
        System.out.println(String.join(", ", toPrint));
    }

    public static LRU getInstance(){
        if(INSTANCE == null){
            INSTANCE = new LRU(2, 1f, true);
        }
        return INSTANCE;
    }
}

