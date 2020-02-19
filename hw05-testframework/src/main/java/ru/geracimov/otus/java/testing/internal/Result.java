package ru.geracimov.otus.java.testing.internal;

public interface Result {

    long getRunCount();

    long getFailureCount();

    long getIgnoreCount();

    long getRunTime();

}
