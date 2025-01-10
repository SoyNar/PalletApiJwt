package com.example.SecuritySpring.Application.DtoResponse;

import com.example.SecuritySpring.Enums.PalletStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PalletResponseDto {
    private Long id;
    private  double maxCapacity;
    private String location;
    private double currentWeight;
    private PalletStatus status;
    private Long carrierId;
    private Long chargeId;

}
