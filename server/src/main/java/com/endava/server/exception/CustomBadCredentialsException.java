package com.endava.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CustomBadCredentialsException extends BadCredentialsException {
    public CustomBadCredentialsException() {
        super("Invalid username or password");
    }
}
