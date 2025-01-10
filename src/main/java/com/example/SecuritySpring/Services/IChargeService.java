package com.example.SecuritySpring.Services;


import com.example.SecuritySpring.Application.DtoRequest.UploadStatusChargeDtoRequest;
import com.example.SecuritySpring.Application.DtoResponse.ChargeResponseDto;
import com.example.SecuritySpring.Application.DtoRequest.ChargeRequestDto;

import java.util.List;

public interface IChargeService {
    public ChargeResponseDto createCharge(ChargeRequestDto requestDto);
    public ChargeResponseDto findChargeId(Long id);
    public List<ChargeResponseDto> getAllCharge();
    public ChargeResponseDto assignToPallet(Long palletId, Long chargeId);
    public ChargeResponseDto deleteCharge(Long id);
    public ChargeResponseDto changeStatus(UploadStatusChargeDtoRequest uploadCharge);
    public ChargeResponseDto reportDamage(Long chargeId);
    public List<ChargeResponseDto> findChargeByCarrier(Long carrierId);
}
