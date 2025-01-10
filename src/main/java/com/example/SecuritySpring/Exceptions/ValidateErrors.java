package com.example.SecuritySpring.Exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidateErrors {
    private String field;
    private String message;
}
