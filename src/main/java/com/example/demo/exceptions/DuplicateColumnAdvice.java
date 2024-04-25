package com.example.demo.exceptions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DuplicateColumnAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Customize the response message based on your application's requirements
        String errorMessage = "Email already exists in the database. Please choose a different email.";

        // You can log the exception for debugging purposes
        ex.printStackTrace();

        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}

