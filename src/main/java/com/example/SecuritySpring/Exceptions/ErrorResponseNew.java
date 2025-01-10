package com.example.SecuritySpring.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponseNew {
    private String status;
    private String message;
    private List<ValidateErrors> errors;
}
