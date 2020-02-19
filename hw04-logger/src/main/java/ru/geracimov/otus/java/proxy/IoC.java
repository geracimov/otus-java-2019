package ru.geracimov.otus.java.proxy;

import ru.geracimov.otus.java.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Random;

public class IoC {
    private static final Random random = new Random();

    public static <C, I> C createProxy(Class<C> aClass, Class<I>[] interfaces) {
        for (Class<I> anInterface : interfaces) {
            if (!anInterface.isAssignableFrom(aClass)) {
                System.err.println("Error assignable!");
                throw new IocException("Class " + aClass + " is not implemented " + anInterface);
            }
        }

        final C instance = newInstance(getDefaultConstructor(aClass));
        final AbstractMethodCache cache = methodCacheRandomChooser();
        final InvocationHandler handler = new LogInvocationHandler<>(instance, cache);

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

    private static AbstractMethodCache methodCacheRandomChooser() {
        final int randomInt = random.nextInt(2);
        AbstractMethodCache result = randomInt > 0
                ? new EqualsMethodCache()
                : new HashMethodCache();
        System.out.println("Choose methodCache: " + result.getClass().getSimpleName());
        return result;
    }

    static class LogInvocationHandler<C> implements InvocationHandler {
        private final C aClass;
        private final AbstractMethodCache cache;

        LogInvocationHandler(C aClass, AbstractMethodCache cache) {
            this.aClass = aClass;
            this.cache = cache;

            for (Method declaredMethod : aClass.getClass().getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(Log.class)) {
                    cache.put(declaredMethod);
                }
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (cache.contains(method)) {
                logParameters(method, args);
            }

            return method.invoke(aClass, args);
        }

        private void logParameters(Method method, Object[] args) {
            System.out.printf("executed method: %s, param: %s\n", method.getName(), Arrays.toString(args));
        }

    }

}
