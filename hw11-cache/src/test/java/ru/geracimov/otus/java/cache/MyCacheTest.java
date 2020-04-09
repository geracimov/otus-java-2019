package ru.geracimov.otus.java.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"Convert2Diamond", "Convert2Lambda"})
public class MyCacheTest {
    private HwCache<String, String> cache;

    @BeforeEach
    public void setUp() {
        CacheManager cacheManager = new CacheManager();
        cache = cacheManager.createCache("temp", String.class, String.class);
    }

    @Test
    public void putTest() {
        cache.put("key1", "value1");
        assertThat(cache.get("key1")).isNotNull().isEqualTo("value1");
        cache.put("key1", "value2");
        assertThat(cache.get("key1")).isNotNull().isEqualTo("value2");
        cache.put("key1", "value3");
        assertThat(cache.get("key1")).isNotNull().isEqualTo("value3");
        cache.put("key1", "value4");
        assertThat(cache.get("key1")).isNotNull().isEqualTo("value4");
    }

    @Test
    public void removeTest() {
        assertThat(cache.get("key1")).isNull();
        cache.remove("key1");
        assertThat(cache.get("key1")).isNull();
        cache.put("key1", "value1");
        cache.remove("key1");
        cache.remove("key1");
        cache.remove("key1");
        assertThat(cache.get("key1")).isNull();
    }

    @Test
    public void getTest() {
        assertThat(cache.get("absentKey")).isNull();
        cache.put("key1", "value4");
        assertThat(cache.get("key1")).isEqualTo("value4");
    }

    @Test
    public void addListener() {
    }

    @Test
    public void removeListener() {
    }

}