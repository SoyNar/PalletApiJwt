package com.example.SecuritySpring.Application.DtoResponse;


import com.example.SecuritySpring.Enums.LoadStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChargeResponseDto {

    private Long id;
    private String dimensions;
    private String description;
    private double weight;
    private LoadStatus chargeStatus;
    private Long palletId;

}
