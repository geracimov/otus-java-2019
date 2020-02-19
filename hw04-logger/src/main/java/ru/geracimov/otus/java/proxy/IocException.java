package ru.geracimov.otus.java.proxy;

public class IocException extends RuntimeException {

    public IocException(String message) {
        super(message);
    }

    public IocException(String message, Throwable cause) {
        super(message, cause);
    }

}
