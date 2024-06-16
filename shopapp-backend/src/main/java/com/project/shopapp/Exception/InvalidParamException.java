package com.project.shopapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidParamException extends RuntimeException{
    public InvalidParamException(String message){
        super(message);
    }
}
