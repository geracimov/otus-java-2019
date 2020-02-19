package ru.geracimov.otus.java.testing.internal.impl;

import ru.geracimov.otus.java.testing.internal.Result;

public class ResultImpl implements Result {
    private long runCount;
    private long failureCount;
    private long ignoreCount;
    private long runTime;

    @Override
    public long getRunCount() {
        return runCount;
    }

    @Override
    public long getFailureCount() {
        return failureCount;
    }

    @Override
    public long getIgnoreCount() {
        return ignoreCount;
    }

    @Override
    public long getRunTime() {
        return runTime;
    }

    public void failed() {
        failureCount++;
    }

    public void run() {
        runCount++;
    }

    public void incIgnoreCount() {
        ignoreCount++;
    }

}
