package ru.geracimov.otus.java.proxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class EqualsMethodCache implements AbstractMethodCache {
    private final Set<Method> loggableMethods;

    public EqualsMethodCache() {
        this.loggableMethods = new HashSet<>();
    }

    @Override
    public boolean contains(Method method) {
        for (Method loggableMethod : loggableMethods) {
            if (methodsEquals(method, loggableMethod)) return true;
        }

        return false;
    }

    @Override
    public void put(Method method) {
        loggableMethods.add(method);
    }

    private boolean methodsEquals(Method first, Method second) {
        if (first.getName().equals(second.getName())) {
            if (!first.getReturnType().equals(second.getReturnType()))
                return false;
            return equalParamTypes(first.getParameterTypes(), second.getParameterTypes());
        }
        return false;
    }

    private boolean equalParamTypes(Class<?>[] first, Class<?>[] second) {
        if (first.length == second.length) {
            for (int i = 0; i < first.length; i++) {
                if (first[i] != second[i])
                    return false;
            }
            return true;
        }
        return false;
    }

}
