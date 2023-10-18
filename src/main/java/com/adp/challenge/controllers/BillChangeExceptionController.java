package com.adp.challenge.controllers;

import com.adp.challenge.exception.BillChangeException;
import com.adp.challenge.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Stream;

@ControllerAdvice
public class BillChangeExceptionController {
    @ExceptionHandler(value = BillChangeException.class)
    public ResponseEntity<Object> handle(BillChangeException billChangeException){
        List<String> errorDetails = Stream.of(billChangeException.getLocalizedMessage()).toList();
        return new ResponseEntity<>(new ErrorResponse("INVALID_BILL_REQUEST", errorDetails),
                HttpStatus.BAD_REQUEST);
    }
}
