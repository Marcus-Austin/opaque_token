package com.austin.opaque_token.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not Found: ");
    }

}
