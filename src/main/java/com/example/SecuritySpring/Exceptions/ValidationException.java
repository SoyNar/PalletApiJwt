package com.example.SecuritySpring.Exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private final List<ValidateErrors> errors;

    public ValidationException(List<ValidateErrors> errors) {
        this.errors = errors;
    }
 }
