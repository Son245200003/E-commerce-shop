package com.project.shopapp.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException (String message){
        super(message);
    }

}
