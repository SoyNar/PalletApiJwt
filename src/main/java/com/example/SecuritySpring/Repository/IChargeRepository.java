package com.example.SecuritySpring.Repository;

import com.example.SecuritySpring.Application.DtoResponse.ChargeResponseDto;
import com.example.SecuritySpring.Application.Model.Charge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChargeRepository extends JpaRepository<Charge, Long> {
    Optional<Charge> findById(Long id);

    @Query("SELECT c FROM Charge c JOIN c.pallet p WHERE p.carrier.id = :carrierId")
    List<Charge> findChargeByCarrierId(Long carrierId);
}
