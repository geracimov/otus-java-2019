package ru.geracimov.otus.java.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CacheManagerTest {
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new CacheManager();
        cacheManager.createCache("existed", String.class, Integer.class);
    }

    @Test
    void createCacheTest() {
        final HwCache<String, String> cache = cacheManager.createCache("aaaa", String.class, String.class);
        assertThat(cache).isNotNull().hasFieldOrProperty("cache");
    }

    @Test
    void getCacheTest() {
        var existed = cacheManager.getCache("existed", String.class, Integer.class);
        assertThat(existed).isNotNull().hasFieldOrProperty("cache");
    }

    @Test
    void createExistedCacheTest() {
        assertThatThrownBy(() -> cacheManager.createCache("existed", Long.class, String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cache 'existed' already exists");
    }

    @Test
    void getAbsentCacheTest() {
        assertThatThrownBy(() -> cacheManager.getCache("notExisted", Long.class, String.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cache 'notExisted' is not exists");
    }

}