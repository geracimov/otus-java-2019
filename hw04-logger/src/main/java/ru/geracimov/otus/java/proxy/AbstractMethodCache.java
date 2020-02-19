package ru.geracimov.otus.java.proxy;

import java.lang.reflect.Method;

public interface AbstractMethodCache {

    boolean contains(Method method);

    void put(Method method);

}
