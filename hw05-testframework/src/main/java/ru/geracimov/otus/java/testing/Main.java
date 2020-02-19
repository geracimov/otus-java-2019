package ru.geracimov.otus.java.testing;

import ru.geracimov.otus.java.testing.internal.TestRunner;

public class Main {

    public static void main(String[] args) {
        TestRunner runner = new TestRunner(ExperimentalClassNoOne.class);
        runner.run();
    }

}
