package com.example.SecuritySpring.Application.DtoRequest;

import com.example.SecuritySpring.Enums.LoadStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChargeRequestDto {

    @NotBlank(message = "Eescriba las dimensiones")
    private String dimensions;
    @NotBlank(message = "Eescriba la descripcion")

    private String description;
    @NotNull(message = "Eescriba la alltura")
    private double weight;
    private Long palletId;

}
