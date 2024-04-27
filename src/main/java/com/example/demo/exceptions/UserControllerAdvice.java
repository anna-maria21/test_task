package com.example.demo.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleJsonParseException(HttpMessageNotReadableException ex) {
        String errorMessage = "Invalid JSON format: " + ex.getMostSpecificCause().getMessage();
        return errorMessage;
    }

    // Хочеться консистентності, якщо логуємо тут - то треба всюдилогувати ексепшени, також круто би було додати логер, в тебе ввже є
    // лоббок,то це одна аноташка @Slf4j і далі юзаєш
    // log.info https://medium.com/@AlexanderObregon/enhancing-logging-with-log-and-slf4j-in-spring-boot-applications-f7e70c6e4cc7
    //Хочеться також прописаного статусу ерори і такі ексепшени краще не хендлить DataIntegrityViolationException
    // треба ще на рівні сервісу запобагти їх появі
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = "Email already exists in the database. Please choose a different email.";
        ex.printStackTrace();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String userNotFoundHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DatesAreWrongException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String datesAreWrong(DatesAreWrongException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(DatesAreNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String datesAreNull(DatesAreNullException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ValidationErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String UserFieldsValidation(ValidationErrorException ex) {
        return ex.getMessage();
    }
}
