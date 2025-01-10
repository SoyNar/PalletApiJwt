package com.example.SecuritySpring.Application.DtoRequest;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PalletRequestDto {

    private  double maxCapacity;
    private String location;
    private double currentWeight;
    private Long carrierId;


}
