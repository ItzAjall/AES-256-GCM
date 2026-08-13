package com.daniyal.exception;

public class InvalidEncryptionException extends RuntimeException {

    public InvalidEncryptionException(String message) {
        super(message);
    }
}