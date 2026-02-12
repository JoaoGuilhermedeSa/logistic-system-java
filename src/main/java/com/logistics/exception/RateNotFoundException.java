package com.logistics.exception;

public class RateNotFoundException extends RuntimeException {

    public RateNotFoundException(String message) {
        super(message);
    }
}
