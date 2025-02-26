package org.example.exceptions;

public class FlowStopException extends RuntimeException {
    public FlowStopException(String message) {
        super(message);
    }
}
