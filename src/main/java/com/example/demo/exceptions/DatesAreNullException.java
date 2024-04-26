package com.example.demo.exceptions;

public class DatesAreNullException extends RuntimeException {
    public DatesAreNullException() {
        super("Dates couldn't be null");
    }
}
