package com.example.SecuritySpring.Services.Impl;

import com.example.SecuritySpring.Application.DtoRequest.PalletRequestDto;
import com.example.SecuritySpring.Application.DtoResponse.PalletResponseDto;
import com.example.SecuritySpring.Application.Model.Pallet;
import com.example.SecuritySpring.Application.Model.Role;
import com.example.SecuritySpring.Application.Model.User;
import com.example.SecuritySpring.Enums.PalletStatus;
import com.example.SecuritySpring.Exceptions.UserNotFoundException;
import com.example.SecuritySpring.Repository.IPalletRepository;
import com.example.SecuritySpring.Repository.IUserRepository;
import com.example.SecuritySpring.Services.IAuditLogService;
import com.example.SecuritySpring.Services.IPalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PalletServiceImpl implements IPalletService {

    @Autowired
     private IUserRepository userRepository;
    @Autowired
    private IPalletRepository palletRepository;
     @Autowired
    private IAuditLogService auditLogService;

    @Override
    public PalletResponseDto createPallet(PalletRequestDto palletDto) {

        validatePalletRequest(palletDto);


        //crear un paller
        Pallet pallets = Pallet.builder()
                .maxCapacity(palletDto.getMaxCapacity())
                .location(palletDto.getLocation())
                .status(PalletStatus.AVAILABLE)
                .currentWeight(palletDto.getCurrentWeight())
                .build();


        if (palletDto.getCarrierId() != null) {
            User carrier = userRepository.findById(palletDto.getCarrierId())
                    .orElseThrow(() -> new UserNotFoundException("Transportista no encontrado"));

            validateCarrier(carrier);


            pallets.setCarrier(carrier);
            pallets.setStatus(PalletStatus.IN_USE);
        }
            Pallet savedPallet = palletRepository.save(pallets);
            auditLogService.logAction("CREATE", "PALLET", savedPallet.getId(),
                    "Created new pallet" + (pallets.getCarrier() != null ?
                            " and assigned to carrier: " + pallets.getCarrier().getUsername() : ""));

            return PalletResponseDto.builder()
                    .id(pallets.getId())
                    .maxCapacity(pallets.getMaxCapacity())
                    .currentWeight(pallets.getCurrentWeight())
                    .location(pallets.getLocation())
                    .status(pallets.getStatus())
                    .chargeId(pallets.getCharge() != null ? pallets.getCharge().getId() : null)
                    .carrierId(pallets.getCarrier() != null ? pallets.getCarrier().getId() : null)
                    .build();
        }

    @Override
    public List<PalletResponseDto> getAllPallet() {
        List<Pallet> pallet = this.palletRepository.findAll();

        //convertir palletsDto en responseDDto
        return pallet.stream()
                .map(pall-> PalletResponseDto.builder()
                        .id(pall.getId())
                        .maxCapacity(pall.getMaxCapacity())
                        .status(pall.getStatus())
                        .location(pall.getLocation())
                        .currentWeight(pall.getCurrentWeight())
                        .chargeId(pall.getCharge() != null ? pall.getCharge().getId() : null)
                        .carrierId(pall.getCarrier().getId())
                        .build())
                .collect(Collectors.toList());
    }

    //metodo para asignar un transportador a un pallet
    @Override
    public PalletResponseDto asignToCarrier(Long palletId, Long carrierId) {

        //verificar que los campos no esten vacios

        if(palletId == null | carrierId == null){
            throw  new IllegalArgumentException("los campos no pueden estar vacios");
        }
        //buscar pallet por id
        Pallet pallet = this.palletRepository.findById(palletId).orElseThrow(()
                -> new UserNotFoundException("palet no existe"));
        User carrier = this.userRepository.findById(carrierId).orElseThrow(()
                -> new UserNotFoundException("usuario no encontrado"));

        //verificar el rol del carrier
       if(carrier.getRole() != Role.CARRIER){
          throw new IllegalArgumentException("El usuario debe ser un transportador");
       }

       //guardar el transpirtista
        pallet.setCarrier(carrier);
       pallet.setStatus(PalletStatus.IN_USE);

       //guardar el pallet

        Pallet savedPallet = palletRepository.save(pallet);

        return PalletResponseDto.builder()
                .id(savedPallet.getId())
                .carrierId(savedPallet.getCarrier().getId())
                .maxCapacity(savedPallet.getMaxCapacity())
                .location(savedPallet.getLocation())
                .build();
    }


    //validaciones del pallet
    private void validatePalletRequest(PalletRequestDto palletDto) {
        if (palletDto.getMaxCapacity() <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser mayor que 0");
        }

        if (palletDto.getCurrentWeight() < 0) {
            throw new IllegalArgumentException("El peso actual no puede ser negativo");
        }

        if (palletDto.getCurrentWeight() > palletDto.getMaxCapacity()) {
            throw new IllegalArgumentException("El peso actual no puede exceder la capacidad máxima");
        }
    }

    private void validateCarrier(User carrier) {
        if (carrier.getRole() != Role.CARRIER) {
            throw new IllegalArgumentException("El usuario debe ser un transportador");
        }
    }



}
