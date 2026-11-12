package com.shopsphere.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

// Spring MVC (@RestControllerAdvice): one place that turns errors into clean JSON replies
// Core Java (Exception handling): we catch errors here instead of in every controller method
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: saree id doesn't exist
    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleNotFound(ProductNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 400: a @Valid rule was broken (price 0, empty name, bad SKU...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        // Java 8 (Streams + Collectors.toMap): list of errors → Map of field → message
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,                       // key: field name
                        FieldError::getDefaultMessage,              // value: what's wrong
                        (first, second) -> first + "; " + second)); // same field broke 2 rules → join them

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Some fields are invalid");
        problem.setProperty("errors", errors);
        return problem;
    }

    // 409: duplicate SKU (the database refused it)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "A saree with this SKU already exists");
    }
}