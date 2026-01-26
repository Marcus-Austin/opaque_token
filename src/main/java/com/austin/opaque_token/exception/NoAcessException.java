package com.austin.opaque_token.exception;
import org.springframework.security.access.AccessDeniedException;

public class NoAcessException extends AccessDeniedException {
    public NoAcessException() {
        super("You don't have access to this resource");
    }

}
