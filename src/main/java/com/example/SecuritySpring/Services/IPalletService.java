package com.example.SecuritySpring.Services;


import com.example.SecuritySpring.Application.DtoRequest.PalletRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.PalletResponseDto;

import java.util.List;

public interface IPalletService {


    //crear un pallet
 public PalletResponseDto createPallet (PalletRequestDto palletDto);
 //obtener todos los pallets  creados
 public List<PalletResponseDto> getAllPallet ();
 //asignar un pallet a un transportador
 public PalletResponseDto asignToCarrier (Long palletId, Long carrierId);
}
