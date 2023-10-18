package com.adp.challenge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse {

    private final int statusCode;
    private final String message;
}
