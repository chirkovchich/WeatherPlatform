package ru.chirkov.exceptions;

public class ApiKeyNotValidException extends Exception {
  public ApiKeyNotValidException(String message) {
    super(message);
  }
}
