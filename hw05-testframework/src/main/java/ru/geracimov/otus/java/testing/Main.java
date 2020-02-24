package ru.geracimov.otus.java.testing;

import ru.geracimov.otus.java.testing.internal.Results;
import ru.geracimov.otus.java.testing.internal.TestsRunner;

public class Main {

    public static void main(String[] args) {
        TestsRunner testsRunner = new TestsRunner(ExperimentalClassNoOne.class);
        final Results results = testsRunner.run();
        results.printStatistic();
    }

}
