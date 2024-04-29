package com.example.demo.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return getErrorsMap(errors);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleJsonParseException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid JSON format: " + ex.getMostSpecificCause().getMessage();
        return new ApiError(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError userNotFoundHandler(UserNotFoundException ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(DatesAreWrongException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError datesAreWrong(DatesAreWrongException ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(DatesAreNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError datesAreNull(DatesAreNullException ex) {
        return new ApiError(ex.getMessage());
    }

    @ExceptionHandler(ValidationErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiError UserFieldsValidation(ValidationErrorException ex) {
        return new ApiError(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateEmailException.class)
    ApiError handleDuplicateEmailException(DuplicateEmailException ex) {
        return new ApiError(ex.getMessage());
    }
}
