package gc.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<K, V> {

    protected final Map<K, SoftReference<V>> cache = new HashMap<>();

    public void put(K key, V value) {
        SoftReference<V> softRef = new SoftReference<>(value);
        cache.put(key, softRef);
    }

    public boolean getCache(K key) {
        boolean rsl = false;
        if (cache.containsKey(key)) {
            rsl = true;
            System.out.println("Entry in cache found: " + cache.get(key));
        } else {
            System.out.println("Entry not found");
        }
        return rsl;
    }

    public V get(K key) {
        V strongRef = cache.getOrDefault(key, new SoftReference<>(null)).get();
        if (strongRef == null) {
            strongRef = load(key);
            put(key, strongRef);
            System.out.println("Values in cache added");
        } else {
            System.out.println("Values from cache: ");
            cache.entrySet().forEach(System.out::println);
        }
            return strongRef;
    }

    protected abstract V load(K key);
}
