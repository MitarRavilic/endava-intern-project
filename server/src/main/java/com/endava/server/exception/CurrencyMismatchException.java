package com.endava.server.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class CurrencyMismatchException extends RuntimeException{

    public CurrencyMismatchException(){
        super("Currency mismatch");
    }
}
