package com.example.SecuritySpring.Controller;

import com.example.SecuritySpring.Application.DtoRequest.ChargeRequestDto;
import com.example.SecuritySpring.Application.DtoRequest.PalletRequestDto;
import com.example.SecuritySpring.Application.DtoRequest.UploadStatusChargeDtoRequest;
import com.example.SecuritySpring.Application.DtoResponse.ChargeResponseDto;
import com.example.SecuritySpring.Application.DtoResponse.PalletResponseDto;
import com.example.SecuritySpring.Exceptions.UserNotFoundException;
import com.example.SecuritySpring.Services.IChargeService;
import com.example.SecuritySpring.Services.IPalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IPalletService palletService;
    @Autowired
    private IChargeService chargeService;
    //crearPallets solo para administradores

    @PostMapping("/createPallet")
    public ResponseEntity<PalletResponseDto> createPallet(@RequestBody  PalletRequestDto requestDto){
        try {
            // Llamada al servicio para crear el pallet
            PalletResponseDto response = palletService.createPallet(requestDto);

            // Retornar una respuesta exitosa con el objeto creado
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            // Manejo de errores si la validación falla (por ejemplo, capacidad máxima <= 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            // Manejo de errores si el transportista no se encuentra
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //metodo para obtener todos los pallets creados
    @GetMapping("/pallets")
    public ResponseEntity<List<PalletResponseDto>> gelAllPallets(){
        List<PalletResponseDto> allPallets = this.palletService.getAllPallet();
        return ResponseEntity.status(HttpStatus.OK).body(allPallets);
    }


    //metodo para asignar un pallet a un transportador

    @PostMapping("{palletId}/asign/{carrierId}")
    public ResponseEntity<PalletResponseDto> asignToCarrier(
            @PathVariable Long palletId,
            @PathVariable Long carrierId){
         PalletResponseDto palletDto = this.palletService.asignToCarrier(palletId,carrierId);
         return ResponseEntity.status(HttpStatus.OK).body(palletDto);
    }

    @PostMapping("/createCharge")
    public ResponseEntity<ChargeResponseDto> createCharge(
            @RequestBody ChargeRequestDto requestDto){
        ChargeResponseDto charge = this.chargeService.createCharge(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(charge);
    }

    //obtener todas las cargas existentes

    @GetMapping("/getCharge")
    public ResponseEntity<List<ChargeResponseDto>> getAllCharges(){
        List<ChargeResponseDto> chargeDto = this.chargeService.getAllCharge();
        return  ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }

    //asignar carga a un pallet

    @PostMapping("/{palletId}/pallet/{chargeId}")
    public ResponseEntity<ChargeResponseDto> assignToPallet(
            @PathVariable Long palletId,
            @PathVariable Long chargeId){
        ChargeResponseDto chargeDto = this.chargeService.assignToPallet(palletId,chargeId);
        return  ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }


    //Obtener carga por id
    @GetMapping("/get/{id}")
    public ResponseEntity<?> getChargesById(
            @PathVariable Long id){
        ChargeResponseDto chargeDto = this.chargeService.findChargeId(id);
        return ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }
    //eliminar una carga

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<?> deleteCharge(
            @PathVariable Long id){
        ChargeResponseDto chargeDto = this.chargeService.deleteCharge(id);
        return  ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }

    //metodo para cambiar estado de carga

    @PutMapping("/update")
    public ResponseEntity<ChargeResponseDto> changeStatusCharge(
            @RequestBody @Valid UploadStatusChargeDtoRequest requestDto){
        ChargeResponseDto chargeDto = this.chargeService.changeStatus(requestDto);

        return  ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }

    //metodo para reportar daños de una carga

    @PutMapping("/report/{chargeId}")
    public ResponseEntity<ChargeResponseDto> reportDamage(
            @PathVariable Long chargeId ){
         ChargeResponseDto chargeDto = this.chargeService.reportDamage(chargeId);
         return ResponseEntity.status(HttpStatus.OK).body(chargeDto);
    }

    // devolver todas las cargas que tiene asignado un transportista
    @GetMapping("/getByCarrier/{carrierId}")
    public ResponseEntity<List<ChargeResponseDto>> findChargeBycarrierId(
            @PathVariable Long carrierId){
        List<ChargeResponseDto> chargeDto = this.chargeService.findChargeByCarrier(carrierId);
        return ResponseEntity.status(HttpStatus.OK).body(chargeDto);

    }
}
