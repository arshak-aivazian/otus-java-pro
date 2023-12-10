package ru.otus.exception;

public class ReflectionException extends RuntimeException {
    public ReflectionException(Throwable cause) {
        super(cause);
    }

    public ReflectionException(String message) {
        super(message);
    }
}
