package com.endava.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidCurrencyCodeException extends RuntimeException{

    public InvalidCurrencyCodeException() {
        super("invalid currency code");
    }
}
