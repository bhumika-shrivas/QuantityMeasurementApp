package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quantity_measurements",
       indexes = {
           @Index(name = "idx_operation", columnList = "operation"),
           @Index(name = "idx_measurement_type", columnList = "thisMeasurementType")
       })
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double thisValue;

    @Column(nullable = false)
    private String thisUnit;

    @Column(nullable = false)
    private String thisMeasurementType;

    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    private String operation;
    private String resultValue;
    private String resultUnit;
    private String errorMessage;

    @Column(nullable = false)
    private boolean isError = false;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}