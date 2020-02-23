package ru.geracimov.otus.java.testing.internal;

import ru.geracimov.otus.java.testing.internal.impl.ResultState;

public interface Result {

    ResultState getState();

    long getRuntime();

    void fail();

    void success();

    void ignore();

}
