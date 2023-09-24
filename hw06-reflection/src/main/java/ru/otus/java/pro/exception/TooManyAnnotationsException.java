package ru.otus.java.pro.exception;

public class TooManyAnnotationsException extends RuntimeException {
    public TooManyAnnotationsException(String message) {
        super(message);
    }
}
