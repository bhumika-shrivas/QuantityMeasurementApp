package com.QuantityMeasurementApp.dto;

import com.QuantityMeasurementApp.Quantity;
import com.QuantityMeasurementApp.IMeasurable;
import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.WeightUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;

/**
 * Simple mapper between QuantityDTO and Quantity<T> objects.
 * This is intentionally minimal — it uses measurementType to pick the category
 * and unit string to resolve the enum constant via fromUnitName.
 */
public final class QuantityMapper {

    private QuantityMapper() {}

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Quantity<?> toQuantity(QuantityDTO dto) {
        if (dto == null) return null;

        String type = dto.getMeasurementType();
        if (type == null) throw new IllegalArgumentException("measurementType is required");

        switch (type.trim().toUpperCase()) {
            case "LENGTH": {
                IMeasurable u = LengthUnit.FEET.fromUnitName(dto.getUnit());
                if (u == null) throw new IllegalArgumentException("Unknown length unit: " + dto.getUnit());
                return new Quantity<>(dto.getValue(), (LengthUnit) u);
            }
            case "WEIGHT": {
                IMeasurable u = WeightUnit.KILOGRAM.fromUnitName(dto.getUnit());
                if (u == null) throw new IllegalArgumentException("Unknown weight unit: " + dto.getUnit());
                return new Quantity<>(dto.getValue(), (WeightUnit) u);
            }
            case "VOLUME": {
                IMeasurable u = VolumeUnit.LITRE.fromUnitName(dto.getUnit());
                if (u == null) throw new IllegalArgumentException("Unknown volume unit: " + dto.getUnit());
                return new Quantity<>(dto.getValue(), (VolumeUnit) u);
            }
            case "TEMPERATURE": {
                IMeasurable u = TemperatureUnit.CELSIUS.fromUnitName(dto.getUnit());
                if (u == null) throw new IllegalArgumentException("Unknown temperature unit: " + dto.getUnit());
                return new Quantity<>(dto.getValue(), (TemperatureUnit) u);
            }
            default:
                throw new IllegalArgumentException("Unsupported measurement type: " + type);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static QuantityDTO fromQuantity(Quantity<?> q) {
        if (q == null) return null;
        return new QuantityDTO(q.getValue(), q.getUnit().getUnitName(), q.getUnit().getMeasurementType());
    }
}
