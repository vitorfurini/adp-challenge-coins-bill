package com.adp.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BillChangeException extends RuntimeException{
    public BillChangeException(String exception){
        super(exception);
    }

    public ResponseEntity<ExceptionResponse> handlerError(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ExceptionResponse(
                        status.value(),
                        message
                ));
    }
}
