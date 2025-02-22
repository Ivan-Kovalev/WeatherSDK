package org.example.exceptions;

public class GetWeatherException extends RuntimeException {
    public GetWeatherException(String message) {
        super(message);
    }
}
