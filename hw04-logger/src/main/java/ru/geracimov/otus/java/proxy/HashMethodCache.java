package ru.geracimov.otus.java.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HashMethodCache implements AbstractMethodCache {
    private final Map<Integer, Method> loggableHashMethods;

    public HashMethodCache() {
        this.loggableHashMethods = new HashMap<>();
    }

    @Override
    public boolean contains(Method method) {
        return loggableHashMethods.containsKey(calcMethodHash(method));
    }

    @Override
    public void put(Method method) {
        loggableHashMethods.put(calcMethodHash(method), method);
    }

    private int calcMethodHash(Method method) {
        // method.hashCode() vs Objects.hashCode(method) vs custom
        final Class<?>[] parameterTypes = method.getParameterTypes();
        return parameterTypes.length == 0
                ? Objects.hash(method.getName(), method.getReturnType())
                : Objects.hash(method.getName(), method.getReturnType(), parameterTypes);
    }
}
