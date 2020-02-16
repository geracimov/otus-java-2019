package ru.geracimov.otus.java;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geracimov.otus.java.proxy.IoC;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class GreatBusinessLogicTest {
    private static final PrintStream OUT = System.out;

    private final ByteArrayOutputStream myOutput = new ByteArrayOutputStream();
    private BusinessLogic original;
    private BusinessLogic proxy;

    @BeforeEach
    void setUp() {
        original = new GreatBusinessLogic();
        Class[] interfaces = new Class[1];
        interfaces[0] = BusinessLogic.class;
        proxy = IoC.createProxy(GreatBusinessLogic.class, interfaces);
        System.setOut(new PrintStream(myOutput));
    }

    @Test
    public void doSomethingAgainOriginalTest() {
        original.doSomethingAgain(5555);
        assertThat(myOutput.toString()).isEqualTo("doSomethingAgain logic started\n" +
                "Do work...  completed!\n");

    }

    @Test
    public void doSomethingAgainProxyTest() {
        proxy.doSomethingAgain(6666);
        assertThat(myOutput.toString()).isEqualTo("executed method: doSomethingAgain, param: [6666]\n" +
                "doSomethingAgain logic started\n" +
                "Do work...  completed!\n");
    }

    @AfterAll
    static void afterAll() {
        System.setOut(OUT);
    }
}