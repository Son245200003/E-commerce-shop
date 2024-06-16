package com.project.shopapp.Exception;

import com.project.shopapp.response.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleCustomRuntimeException(DataNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        // Bạn có thể trả về một đối tượng JSON hoặc một thông báo lỗi tùy chỉnh
    }
    @ExceptionHandler(PermissionDenyException.class)
    @ResponseBody
    public ResponseEntity<?> handlePermissionDenyException(PermissionDenyException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(InvalidParamException.class)
    @ResponseBody
    public ResponseEntity<?> handlePermissionDenyException(InvalidParamException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}
