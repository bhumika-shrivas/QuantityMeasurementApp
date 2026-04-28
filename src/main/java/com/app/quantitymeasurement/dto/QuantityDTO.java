package com.app.quantitymeasurement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

    @NotNull(message = "value is required")
    private Double value;

    @NotEmpty(message = "unit is required")
    private String unit;

    @NotEmpty(message = "measurementType is required")
    @Pattern(
        regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit",
        message = "measurementType must be LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit"
    )
    private String measurementType;

    private String operationType;

    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isUnitValidForType() {
        if (unit == null || measurementType == null) return true;
        return switch (measurementType) {
            case "LengthUnit"      -> unit.matches("FEET|INCHES|YARD");
            case "WeightUnit"      -> unit.matches("GRAM|KILOGRAM|TONNE");
            case "VolumeUnit"      -> unit.matches("MILLILITER|LITER|KILOLITER|GALLON");
            case "TemperatureUnit" -> unit.matches("CELSIUS|FAHRENHEIT|KELVIN");
            default -> false;
        };
    }
}