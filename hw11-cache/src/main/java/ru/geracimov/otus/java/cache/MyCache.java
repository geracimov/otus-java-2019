package ru.geracimov.otus.java.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.WeakHashMap;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K, V> cache;
    private final WeakHashMap<HwListener<K, V>, Void> listeners;

    MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyAllListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        V value;
        if ((value = cache.get(key)) != null) {
            cache.remove(key);
            notifyAllListeners(key, value, "REMOVE");
        }
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.put(listener, null);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyAllListeners(K key, V value, String action) {
        listeners.keySet().forEach(l -> {
            try {
                l.notify(key, value, action);
            } catch (Exception e) {
                final String format = String.format("Invocation listener '%s' exception on %s with key=%s, value=%s",
                                                    l.getClass().getName(), action, key, value);
                log.warn(format, e);
            }
        });
    }

}
