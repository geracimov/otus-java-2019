package ru.geracimov.otus.java.testing.internal;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.geracimov.otus.java.testing.annotation.*;
import ru.geracimov.otus.java.testing.internal.impl.ResultsImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TestsRunner {
    private final Class<?> testClass;
    private final Set<Method> beforeAllMethods;
    private final Set<Method> beforeEachMethods;
    private final Set<Method> testMethods;
    private final Set<Method> afterEachMethods;
    private final Set<Method> afterAllMethods;
    private final Results results;

    public TestsRunner(Class<?> testClass) {
        this.testClass = testClass;
        beforeAllMethods = new HashSet<>();
        beforeEachMethods = new HashSet<>();
        testMethods = new HashSet<>();
        afterEachMethods = new HashSet<>();
        afterAllMethods = new HashSet<>();
        results = new ResultsImpl();
    }

    @SneakyThrows
    public Results run() {
        initMethodAnnotations();
        invokeMethods(beforeAllMethods);
        for (Method testMethod : testMethods) {
            System.out.println("-------");
            TestRunner runner = new TestRunner(testClass, testMethod, beforeEachMethods, afterEachMethods);
            results.add(testMethod, runner.run());
        }
        invokeMethods(afterAllMethods);
        return results;
    }

    private void invokeMethods(Set<Method> beforeAllMethods) throws IllegalAccessException, InvocationTargetException {
        for (Method m : beforeAllMethods) {
            m.invoke(null);
        }
    }

    private void initMethodAnnotations() {
        for (Method method : testClass.getDeclaredMethods()) {
            final int modifiers = method.getModifiers();
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                if (annotation.annotationType().isAssignableFrom(Test.class)) {
                    testMethods.add(method);
                } else if (annotation.annotationType().isAssignableFrom(BeforeEach.class)) {
                    beforeEachMethods.add(method);
                } else if (annotation.annotationType().isAssignableFrom(AfterEach.class)) {
                    afterEachMethods.add(method);
                } else if (annotation.annotationType().isAssignableFrom(AfterAll.class)) {
                    if (!Modifier.isStatic(modifiers)) {
                        log.warn("Method {} must be static. Skip...", method.getName());
                    } else afterAllMethods.add(method);
                } else if (annotation.annotationType().isAssignableFrom(BeforeAll.class)) {
                    if (!Modifier.isStatic(modifiers)) {
                        log.warn("Method {} must be static. Skip...", method.getName());
                    } else beforeAllMethods.add(method);
                }
            }
        }
    }

}