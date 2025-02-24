package ru.chirkov.exceptions;

public class ApiKeyIsUsed extends Exception {
    public ApiKeyIsUsed(String message) {
        super(message);
    }
}
