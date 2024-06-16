package com.project.shopapp.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class PermissionDenyException  extends RuntimeException {
    public PermissionDenyException(String message) {
        super(message);
    }
}
