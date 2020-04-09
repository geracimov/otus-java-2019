package ru.geracimov.otus.java.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    public void addListenerTest() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        HwListener<String, String> listener = new HwListener<String, String>() {
            @Override
            public void notify(String key, String value, String action) {
                printStream.print(String.format("key:%s, value:%s, action: %s", key, value, action));
            }
        };
        cache.addListener(listener);
        cache.put("111", "222");
        assertThat(out.toString()).isEqualTo("key:111, value:222, action: PUT");
        out.reset();
        cache.put("222", "333");
        cache.remove("222");
        assertThat(out.toString()).isEqualTo("key:222, value:333, action: PUT" + "key:222, value:333, action: REMOVE");
    }

    @Test
    public void removeListenerTest() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        HwListener<String, String> listener = new HwListener<String, String>() {
            @Override
            public void notify(String key, String value, String action) {
                printStream.print(String.format("key:%s, value:%s, action: %s", key, value, action));
            }
        };
        cache.addListener(listener);
        cache.put("111", "222");
        assertThat(out.toString()).isEqualTo("key:111, value:222, action: PUT");
        out.reset();
        cache.removeListener(listener);
        cache.put("222", "111");
        assertThat(out.toString()).isEmpty();
    }

}