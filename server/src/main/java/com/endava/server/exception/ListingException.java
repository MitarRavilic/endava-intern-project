package com.endava.server.exception;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class ListingException extends RuntimeException{

    public ListingException(String message){
        super(message);
    }
}
