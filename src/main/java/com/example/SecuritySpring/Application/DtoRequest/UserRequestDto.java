package com.example.SecuritySpring.Application.DtoRequest;

import com.example.SecuritySpring.Application.Model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {

    @NotBlank(message = "el username es obligatorio ")
    @Email(message = "el correo no es valido")
    private String username;
    @NotBlank(message = "el nombre es obligatorio ")
    private String  fullname;
    @NotBlank(message = "la contraseña es obligatoria ")
    private String password;
    private Role role;


}
