package ru.chirkov.exceptions;

public class WeatherHttpClientException extends Exception {
    public WeatherHttpClientException(String message) {
        super(message);
    }
}
