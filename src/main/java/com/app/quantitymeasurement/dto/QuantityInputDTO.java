package com.app.quantitymeasurement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @NotNull(message = "thisQuantityDTO is required")
    @Valid
    private QuantityDTO thisQuantityDTO;

    @Valid
    private QuantityDTO thatQuantityDTO;

    private String targetUnit;
}