package org.example.exceptions;

public class UpdateWeatherException extends RuntimeException {
    public UpdateWeatherException(String message) {
        super(message);
    }
}
