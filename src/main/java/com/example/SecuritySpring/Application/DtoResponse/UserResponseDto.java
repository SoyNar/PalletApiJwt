package com.example.SecuritySpring.Application.DtoResponse;
import com.example.SecuritySpring.Application.Model.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String fullname;
    private Role role;
}
