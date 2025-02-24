package ru.chirkov.exceptions;

public class ApiKeyIsEmptyException extends Exception {
    public ApiKeyIsEmptyException(String message) {
        super(message);
    }
}
