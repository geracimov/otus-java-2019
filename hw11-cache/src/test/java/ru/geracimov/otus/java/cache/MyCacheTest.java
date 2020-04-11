package ru.geracimov.otus.java.cache;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"Convert2Diamond", "Convert2Lambda"})
@ExtendWith(MockitoExtension.class)
public class MyCacheTest {
    private HwCache<String, String> cache;

    @Mock
    Appender mockedAppender = Mockito.mock(Appender.class);

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
    @DisplayName("Кеш отработает успешно при возникновении усключения в лиснере и запишет в лог")
    public void addListenerExceptionTest() {
        final ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(MyCache.class);
        logger.addAppender(mockedAppender);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        HwListener<String, String> listener = new HwListener<String, String>() {
            @Override
            public void notify(String key, String value, String action) {
                throw new RuntimeException("Some error");
            }
        };
        cache.addListener(listener);
        cache.put("111", "222");

        ArgumentCaptor<Appender> argumentCaptor = ArgumentCaptor.forClass(Appender.class);
        Mockito.verify(mockedAppender).doAppend(argumentCaptor.capture());
        assertThat(argumentCaptor.getAllValues().size()).isEqualTo(1);
        final LoggingEvent loggingEvent = (LoggingEvent) argumentCaptor.getAllValues().get(0);
        assertThat(loggingEvent.getLevel()).isEqualTo(Level.WARN);
        assertThat(loggingEvent.getMessage()).isEqualTo(
                "Invocation listener 'ru.geracimov.otus.java.cache.MyCacheTest$2' exception on PUT with key=111, value=222");
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