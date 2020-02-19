package ru.geracimov.otus.java.testing;

import ru.geracimov.otus.java.testing.annotation.*;

public class ExperimentalClassNoOne {


    @BeforeAll
    public static void beforeAllNoOne() {

    }

    @BeforeAll
    public static void beforeAllNoTwo() {

    }

    @BeforeEach
    public void beforeEachNoOne() {

    }

    @BeforeEach
    public void beforeEachNoTwo() {

    }

    //------------------------------------
    @Test
    public void testPublicVoid() {

    }

    @Test
    public String testPublicString() {
        return "testPublicString started";
    }

    @Test
    public static void testPublicStaticVoid() {

    }

    @Test
    private void testPrivateVoid() {

    }

    @Test
    private static void testPrivateStaticVoid() {

    }

    //------------------------------------
    @AfterEach
    public void afterEachNoOne() {

    }

    @AfterEach
    public void afterEachNoTwo() {

    }

    @AfterAll
    public void afterAllNoOne() {

    }

    @AfterAll
    public static void afterAllNoTwo() {

    }

}
