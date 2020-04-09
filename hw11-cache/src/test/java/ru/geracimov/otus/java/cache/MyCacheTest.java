package ru.geracimov.otus.java.cache;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyCacheTest {
    private static CacheManager cacheManager;
    private HwCache<String, String> cache;

    @BeforeAll
    static void beforeAll() {
        cacheManager = new CacheManager();
    }

    @BeforeEach
    public void setUp() {
        cache = cacheManager.createCache("temp", String.class, String.class);
    }

    @Test
    public void put() {
        cache.put("key1", "value1");
        assertThat(cache.get("key1")).isNotNull();
    }

    @Test
    public void remove() {
    }

    @Test
    public void get() {
    }

    @Test
    public void addListener() {
    }

    @Test
    public void removeListener() {
    }

}