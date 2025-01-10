package com.example.SecuritySpring.Application.Model;

import com.example.SecuritySpring.Enums.LoadStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "charge")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String dimensions;
    @Column(nullable = false)

    private String description;

    @Column(nullable = false)
    private double weight;



    @Enumerated(EnumType.STRING)
    private LoadStatus status;

    @OneToOne
    @JoinColumn(name = "pallet_id")
    private Pallet pallet;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
