package ru.geracimov.otus.java.testing.exception;

public class FrameworkException extends RuntimeException{

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

}
