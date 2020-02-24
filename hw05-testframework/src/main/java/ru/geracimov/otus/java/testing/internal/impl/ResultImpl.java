package ru.geracimov.otus.java.testing.internal.impl;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.geracimov.otus.java.testing.exception.FrameworkException;
import ru.geracimov.otus.java.testing.internal.Result;

import java.lang.reflect.Method;


@ToString
@RequiredArgsConstructor
public class ResultImpl implements Result {
    private final Method method;
    private final long started = System.nanoTime();
    private ResultState state;
    private long finished;
    private Throwable exception;

    @Override
    public ResultState getState() {
        return state;
    }

    @Override
    public long getRuntime() {
        if (state.equals(ResultState.INIT))
            throw new FrameworkException("Test don't completed");
        return finished - started;
    }

    @Override
    public void fail(Throwable throwable) {
        state = ResultState.FAILURE;
        exception = throwable;
        finished = System.nanoTime();
    }

    @Override
    public Throwable failCause() {
        return exception;
    }

    @Override
    public void success() {
        state = ResultState.SUCCESS;
        finished = System.nanoTime();
    }

    @Override
    public void ignore() {
        state = ResultState.IGNORE;
        finished = System.nanoTime();
    }

}
