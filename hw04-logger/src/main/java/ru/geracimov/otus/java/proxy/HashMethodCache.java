package ru.geracimov.otus.java.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HashMethodCache implements AbstractMethodCache {
    private final Set<Integer> loggableHashMethods;

    public HashMethodCache() {
        this.loggableHashMethods = new HashSet<>();
    }

    @Override
    public boolean contains(Method method) {
        return loggableHashMethods.contains(calcMethodHash(method));
    }

    @Override
    public void put(Method method) {
        loggableHashMethods.add(calcMethodHash(method));
    }

    private int calcMethodHash(Method method) {
        return Objects.hash(method.getName(), Arrays.deepHashCode(method.getParameterTypes()));
    }
}
