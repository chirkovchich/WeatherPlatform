package ru.chirkov.exceptions;

public class WeatherApiInstanceIsShutdown extends Exception {
    public WeatherApiInstanceIsShutdown(String message) {
        super(message);
    }
}
