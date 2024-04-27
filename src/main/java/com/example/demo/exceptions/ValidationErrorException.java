package com.example.demo.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationErrorException extends RuntimeException {
    public ValidationErrorException(BindingResult result) {
        super(String.valueOf(result));
    }
}
