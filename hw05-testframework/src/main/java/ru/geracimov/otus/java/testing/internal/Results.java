package ru.geracimov.otus.java.testing.internal;

import java.lang.reflect.Method;

public interface Results {

    void printStatistic();

    void add(Method method, Result result);

}
