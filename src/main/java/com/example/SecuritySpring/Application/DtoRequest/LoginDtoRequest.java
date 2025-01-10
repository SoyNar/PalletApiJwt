package com.example.SecuritySpring.Application.DtoRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDtoRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es valido")
    private String username;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

}
