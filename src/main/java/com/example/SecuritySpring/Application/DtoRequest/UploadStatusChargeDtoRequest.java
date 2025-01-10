package com.example.SecuritySpring.Application.DtoRequest;

import com.example.SecuritySpring.Enums.LoadStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadStatusChargeDtoRequest {

    private LoadStatus status;
    private LoadStatus newStatus;
    private Long chargeId;
}
