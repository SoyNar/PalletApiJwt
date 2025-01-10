package com.example.SecuritySpring.Services.Impl;

import com.example.SecuritySpring.Application.DtoRequest.UploadStatusChargeDtoRequest;
import com.example.SecuritySpring.Application.DtoResponse.ChargeResponseDto;
import com.example.SecuritySpring.Application.DtoRequest.ChargeRequestDto;
import com.example.SecuritySpring.Application.Model.Charge;
import com.example.SecuritySpring.Application.Model.Pallet;
import com.example.SecuritySpring.Application.Model.User;
import com.example.SecuritySpring.Enums.LoadStatus;
import com.example.SecuritySpring.Exceptions.UserNotFoundException;
import com.example.SecuritySpring.Exceptions.ValidateErrors;
import com.example.SecuritySpring.Exceptions.ValidationException;
import com.example.SecuritySpring.Repository.IChargeRepository;
import com.example.SecuritySpring.Repository.IPalletRepository;
import com.example.SecuritySpring.Repository.IUserRepository;
import com.example.SecuritySpring.Services.IAuditLogService;
import com.example.SecuritySpring.Services.IChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChargeServiceImpl implements IChargeService {

    @Autowired
     private IChargeRepository chargeRepository;

    @Autowired
    private IPalletRepository palletRepository;

    @Autowired
    private IAuditLogService auditService;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public ChargeResponseDto createCharge(ChargeRequestDto requestDto) {
        Charge charge = Charge.builder()
                .dimensions(requestDto.getDimensions())
                .description(requestDto.getDescription())
                .weight(requestDto.getWeight())
                .status(LoadStatus.PENDING)
                .build();


 Charge savedCharge = chargeRepository.save(charge);

        return buildChargeResponse(savedCharge);

    }

    @Override
    public ChargeResponseDto findChargeId(Long id) {
         Charge chargeId = chargeRepository.findById(id).orElseThrow(()
                 -> new UserNotFoundException("Carga no existe"));

        return buildChargeResponse(chargeId);

    }

    @Override
    public List<ChargeResponseDto> getAllCharge() {
        List<Charge> charge = this.chargeRepository.findAll();
        return charge.stream()
                .map(charges -> ChargeResponseDto
                        .builder()
                        .id(charges.getId())
                        .description(charges.getDescription())
                        .palletId(charges.getId())
                        .dimensions(charges.getDimensions())
                        .weight(charges.getWeight())
                        .chargeStatus(charges.getStatus())
                        .palletId(charges.getPallet() != null ? charges.getPallet().getId() : null )
                        .build()
                ).collect(Collectors.toList());
    }

    @Override
    public ChargeResponseDto assignToPallet(Long palletId, Long chargeId) {

        Charge charge = this.chargeRepository.findById(chargeId).orElseThrow(()
                -> new UserNotFoundException("Carga no existe"));

        Pallet pallet = this.palletRepository.findById(palletId).orElseThrow(()
                ->new UserNotFoundException("pallet no existe"));
        if(pallet.getCharge() != null){
            throw  new IllegalArgumentException("el pallet ya tiene una carga asignada");

        }
        if(!pallet.canAcceptCharge(charge.getWeight())){
            throw  new IllegalArgumentException("EL tamaño de la carga excede el del pallet");
        }

        charge.setStatus(LoadStatus.IN_TRANSIT);
        //guardar

        charge.setPallet(pallet);
        pallet.setCharge(charge);

        Charge charges = this.chargeRepository.save(charge);
        auditService.logAction("ASSIGN", "LOAD", charges.getId(),
                "Assigned to pallet: " + pallet.getId());

        return buildChargeResponse(charges);

    }

    @Override
    public ChargeResponseDto deleteCharge(Long id) {

        Charge charges = this.chargeRepository.findById(id).orElseThrow(()
                -> new UserNotFoundException("Carga no encontrada"));

        this.chargeRepository.delete(charges);
        return buildChargeResponse(charges);
    }

    @Override
    public ChargeResponseDto changeStatus(UploadStatusChargeDtoRequest request) {

        //lista de errores
        List<ValidateErrors> errors = new ArrayList<>();

        //validacuones
        validateInitialFields(request, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        //Logica de negocio
        Charge charge = chargeRepository.findById(request.getChargeId())
                .orElseThrow(() -> new UserNotFoundException("Carga no encontrada con ID: " + request.getChargeId()));

        // Verificar estado anterior y actualizar
        LoadStatus oldStatus = charge.getStatus();
        validateStatusTransition(oldStatus, request.getNewStatus(), errors);
        charge.setStatus(request.getNewStatus());

        Charge savedLoad = chargeRepository.save(charge);

        // Guardar acción de auditoría
        auditService.logAction("STATUS_UPDATE", "LOAD", savedLoad.getId(),
                "Status updated from " + oldStatus + " to " + request.getNewStatus());

        // Crear ResponseDTO para la respuesta

        return buildChargeResponse(savedLoad);
     }

    @Override
    public ChargeResponseDto reportDamage(Long chargeId) {

        // buscar la carga por su id
        //validaciones
        List<ValidateErrors> errors = validateDamageReport(chargeId);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        //logica de negocio
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new UserNotFoundException("Carga no encontrada con ID: " + chargeId));

        LoadStatus oldStatus = charge.getStatus();
        validateStatusTransition(oldStatus, LoadStatus.DAMAGED, errors);

        charge.setStatus(LoadStatus.DAMAGED);
        Charge savedLoad = this.chargeRepository.save(charge);

        return buildChargeResponse(savedLoad);
    }

    @Override
    public List<ChargeResponseDto> findChargeByCarrier(Long carrierId) {


        if (carrierId == null) {
            List<ValidateErrors> errors = List.of(new ValidateErrors("carrierId", "El id del transportador no puede estar vacío"));
            throw new ValidationException(errors);
        }

        User carrier = this.userRepository.findById(carrierId).orElseThrow(()
                -> new UserNotFoundException("El transportista no existe"));
        //buscar carga por el id del transportista

        List<Charge> charges = this.chargeRepository.findChargeByCarrierId(carrierId);

        List<ChargeResponseDto> chargeDtos = charges.stream()
                .map(charge -> ChargeResponseDto.builder()
                        .id(charge.getId())
                        .dimensions(charge.getDimensions())
                        .description(charge.getDescription())
                        .weight(charge.getWeight())
                        .chargeStatus(charge.getStatus())
                        .palletId(charge.getPallet() != null ? charge.getPallet().getId() : null )
                        .build()
                )
                .toList();

        return chargeDtos;


    }


    //validar el estado de la carga si es completada no se sigue con el cambio de status
    private void validateStatusTransition(LoadStatus oldStatus, LoadStatus newStatus, List<ValidateErrors> errors) {
        if (oldStatus == LoadStatus.DELIVERED && newStatus != LoadStatus.DELIVERED) {
            errors.add(new ValidateErrors("statusTransition",
                    "No se puede cambiar el estado de una carga entregada"));
        }
    }

//validadno el id de la carga que este en el request
    //validando que el nuevo estado de la carga este presente en el request
    private void validateInitialFields(UploadStatusChargeDtoRequest request, List<ValidateErrors> errors) {
        if (request.getChargeId() == null) {
            errors.add(new ValidateErrors("chargeId", "Proporcione el id de la carga"));
        }
        if (request.getNewStatus() == null) {
            errors.add(new ValidateErrors("statusNew", "El estado de carga no puede estar vacío"));
        }
    }

    // metodo para convertir un modelo Charge a un responseDto
    private ChargeResponseDto buildChargeResponse(Charge charge) {
        return ChargeResponseDto.builder()
                .chargeStatus(charge.getStatus())
                .description(charge.getDescription())
                .id(charge.getId())
                .weight(charge.getWeight())
                .description(charge.getDescription())
                .palletId(charge.getPallet() != null ? charge.getPallet().getId() : null )
                .dimensions(charge.getDimensions())
                .build();
    }

    private List<ValidateErrors> validateDamageReport(Long chargeId) {
        List<ValidateErrors> errors = new ArrayList<>();
        if (chargeId == null) {
            errors.add(new ValidateErrors("chargeId", "Charge ID is required"));
        }
        return errors;
       }
    }


