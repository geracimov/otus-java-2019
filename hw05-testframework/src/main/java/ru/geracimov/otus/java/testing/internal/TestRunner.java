package ru.geracimov.otus.java.testing.internal;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestRunner implements Runnable {
    private final Class<?> testClass;

    public TestRunner(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void run() {
        for (Method method : testClass.getDeclaredMethods()) {
            System.out.println("method = " + method);
            final int modifiers = method.getModifiers();
            System.out.println("method.getModifiers() = " + modifiers);
            System.out.println("Modifier.isStatic(modifiers) = " + Modifier.isStatic(modifiers));
            System.out.println("Modifier.isFinal(modifiers) = " + Modifier.isFinal(modifiers));
            System.out.println("method.getParameterCount() = " + method.getParameterCount());
            for (Annotation annotation : method.getDeclaredAnnotations()) {
                System.out.println("annotation = " + annotation);
            }
            System.out.println("-*--------------------");
        }
    }

}
