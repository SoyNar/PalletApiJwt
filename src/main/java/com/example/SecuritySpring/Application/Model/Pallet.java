package com.example.SecuritySpring.Application.Model;

import com.example.SecuritySpring.Enums.PalletStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "pallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)

    private double maxCapacity;
    @Column(nullable = true)

    private String location;
    @Column(nullable = false)

    private double currentWeight;
    @Enumerated(EnumType.STRING)
    private PalletStatus status;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private User carrier;

    @OneToOne(mappedBy = "pallet", cascade = CascadeType.ALL)
    private Charge charge ;

    // Método para verificar si puede aceptar una carga
    public boolean canAcceptCharge (double chargeWeight) {
        return chargeWeight <= maxCapacity;
    }


}
