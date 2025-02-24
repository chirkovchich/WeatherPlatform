package ru.chirkov.exceptions;

public class ApiCallLimitException extends Exception {
    public ApiCallLimitException(String message) {
        super(message);
    }
}
