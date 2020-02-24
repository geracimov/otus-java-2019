package ru.geracimov.otus.java.testing.internal;

import ru.geracimov.otus.java.testing.internal.impl.ResultState;

public interface Result {

    ResultState getState();

    long getRuntime();

    void fail(Throwable e);

    Throwable failCause();

    void success();

    void ignore();

}
