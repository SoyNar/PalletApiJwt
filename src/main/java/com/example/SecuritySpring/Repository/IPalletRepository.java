package com.example.SecuritySpring.Repository;

import com.example.SecuritySpring.Application.Model.Pallet;
import com.example.SecuritySpring.Enums.PalletStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPalletRepository extends JpaRepository<Pallet,Long> {
    List<Pallet> findByCarrierId(Long carrierId);
    List<Pallet> findByStatus(PalletStatus status);
    Optional<Pallet> findById(Long id);
    List<Pallet> findByCarrierIdAndStatus(Long carrierId, PalletStatus status);
}
