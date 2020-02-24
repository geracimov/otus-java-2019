package ru.geracimov.otus.java.testing;

import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.testing.annotation.*;
import ru.geracimov.otus.java.testing.exception.FrameworkException;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExperimentalClassNoOne {
    private static final Random random = new Random();

    @BeforeAll
    public static void beforeAllNoOne() {
        log.info("beforeAllNoOne");
    }

    @BeforeAll
    public static void beforeAllNoTwo() {
        log.info("beforeAllNoTwo");
    }

    @BeforeEach
    public void beforeEachNoOne() {
        log.info("beforeEachNoOne");
    }

    @BeforeEach
    public void beforeEachNoTwo() {
        log.info("beforeEachNoTwo");
    }

    //------------------------------------
    @Test
    @DisplayName("Тест testPrivateVoidException бросает исключение")
    private void testPrivateVoidException() {
        hardWork(20, 200);
        throw new FrameworkException("testPrivateVoidException thrown");
    }

    @Test
    public void testPublicVoid() {
        hardWork(20, 200);
    }

    @Test
    public String testPublicString() {
        hardWork(20, 200);
        return "testPublicString result";
    }

    @Test
    @DisplayName("Запуск public static void теста завершается игнорированием")
    public static void testPublicStaticVoid() {
        hardWork(20, 200);
    }

    @Test
    private void testPrivateVoid() {
        hardWork(20, 200);
    }

    @Test
    @DisplayName("Запуск private static void теста завершается игнорированием")
    private static void testPrivateStaticVoid() {
        hardWork(20, 200);
    }

    //------------------------------------
    @AfterEach
    public void afterEachNoOne() {
        log.info("afterEachNoOne");
    }

    @AfterEach
    public void afterEachNoTwo() {
        log.info("afterEachNoTwo");
    }

    @AfterAll
    public void afterAllNoOne() {
        log.info("afterAllNoOne");
    }

    @AfterAll
    public static void afterAllNoTwo() {
        log.info("afterAllNoTwo");
    }

    private static void hardWork(int from, int to) {
        log.debug("Do work ...  ");
        final int millis = from + random.nextInt(to - from);
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
