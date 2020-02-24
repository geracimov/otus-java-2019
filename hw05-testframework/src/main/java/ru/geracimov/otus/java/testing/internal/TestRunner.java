package ru.geracimov.otus.java.testing.internal;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.testing.exception.FrameworkException;
import ru.geracimov.otus.java.testing.internal.impl.ResultImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;

import static java.lang.reflect.Modifier.isPrivate;

@Slf4j
@RequiredArgsConstructor
public class TestRunner {
    private final Class<?> testClass;
    private final Method method;
    private final Collection<Method> beforeEachMethods;
    private final Collection<Method> afterEachMethods;

    @SneakyThrows
    public Result run() {
        final Object o = getDefaultConstructor().newInstance();
        for (Method beforeEachMethod : beforeEachMethods) {
            beforeEachMethod.invoke(o);
        }

        log.info("Test {} [{}] started", method.getName(), Modifier.toString(method.getModifiers()));
        Result result = new ResultImpl(method);
        if (Modifier.isStatic(method.getModifiers())) {
            result.ignore();
            log.warn("Method {} cannot be static. Skip...", method.getName());
            return result;
        }

        if (isPrivate(method.getModifiers()))
            method.setAccessible(true);

        try {
            method.invoke(o);
            result.success();
        } catch (Exception e) {
            result.fail(e.getCause());
        }

        log.info("Test {} completed", method.getName());

        for (Method afterEachMethod : afterEachMethods) {
            afterEachMethod.invoke(o);
        }

        return result;
    }


    private Constructor<?> getDefaultConstructor() {
        final Constructor<?> constructor;
        try {
            constructor = testClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new FrameworkException("Default constructor is not available " + testClass.getSimpleName(), e);
        }
        return constructor;
    }

}
