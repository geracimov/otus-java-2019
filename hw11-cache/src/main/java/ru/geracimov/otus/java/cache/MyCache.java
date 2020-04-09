package ru.geracimov.otus.java.cache;

import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final WeakHashMap<K, V> cache;

    MyCache(WeakHashMap<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {

    }

    @Override
    public void removeListener(HwListener<K, V> listener) {

    }

}
