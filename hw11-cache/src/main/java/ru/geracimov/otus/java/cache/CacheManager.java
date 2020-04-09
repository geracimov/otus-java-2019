package ru.geracimov.otus.java.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@SuppressWarnings("unchecked")
public class CacheManager {
    private final Map<String, HwCache<?, ?>> caches;

    public CacheManager() {
        caches = new HashMap<>();
    }

    public <K, V> HwCache<K, V> createCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        if (this.caches.get(cacheName) != null) {
            throw new IllegalArgumentException("Cache '" + cacheName + "' already exists");
        } else {
            return (HwCache<K, V>) this.caches.computeIfAbsent(cacheName, s -> new MyCache<K, V>(new WeakHashMap<>()));
        }
    }

    public <K, V> HwCache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
        if (this.caches.get(cacheName) == null) {
            throw new IllegalArgumentException("Cache '" + cacheName + "' is not exists");
        }
        return (HwCache<K, V>) this.caches.get(cacheName);
    }

    public void clear(){
        caches.clear();
    }

}