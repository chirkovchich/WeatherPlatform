package ru.chirkov.exceptions;

public class CityIsEmptyException extends Exception {
    public CityIsEmptyException(String message) {
        super(message);
    }
}
