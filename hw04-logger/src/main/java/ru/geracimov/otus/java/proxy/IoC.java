package ru.geracimov.otus.java.proxy;

import ru.geracimov.otus.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class IoC {

    public static <C, I> C createProxy(Class<C> aClass, Class<I>[] interfaces) {
        for (Class<I> anInterface : interfaces) {
            if (!anInterface.isAssignableFrom(aClass)) {
                System.err.println("Error assignable!");
                throw new IocException("Class " + aClass + " is not implemented " + anInterface);
            }
        }

        final Constructor<C> constructor = getDefaultConstructor(aClass);
        final C instance = newInstance(constructor);
        final InvocationHandler handler = new LogInvocationHandler<>(instance);

        //noinspection unchecked,unchecked
        return (C) Proxy.newProxyInstance(IoC.class.getClassLoader(), interfaces, handler);
    }

    private static <C> C newInstance(Constructor<C> constructor) {
        final C object;
        try {
            object = constructor.newInstance();
        } catch (Exception e) {
            throw new IocException("Cannot create object", e);
        }
        return object;
    }

    private static <C> Constructor<C> getDefaultConstructor(Class<C> aClass) {
        final Constructor<C> constructor;
        try {
            constructor = aClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IocException("Default constructor is not available " + aClass.getSimpleName(), e);
        }
        return constructor;
    }


    static class LogInvocationHandler<C> implements InvocationHandler {
        private final C aClass;
        private final Set<Method> loggableMethods;

        LogInvocationHandler(C aClass) {
            this.aClass = aClass;
            this.loggableMethods = new HashSet<>();
            // cache all loggable methods
            for (Method declaredMethod : aClass.getClass().getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(Log.class)) {
                    loggableMethods.add(declaredMethod);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLoggable(method)) {
                logParameters(method, args);
            }

            return method.invoke(aClass, args);
        }

        private boolean isLoggable(Method method) {
            for (Method loggableMethod : loggableMethods) {
                if (methodsEquals(method, loggableMethod)) return true;
            }
            return false;
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

        private void logParameters(Method method, Object[] args) {
            System.out.printf("executed method: %s, param: %s\n", method.getName(), Arrays.toString(args));
        }

    }

}
