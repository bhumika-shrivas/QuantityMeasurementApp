package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

/**
 * VolumeUnit represents supported volume units and conversion logic.
 *
 * Base unit used internally: LITER
 */
public enum VolumeUnit implements IMeasurable {

    // Base unit: liter
    LITER(1.0),
    // Milliliter: 0.001 liter
    MILLILITER(0.001),
    // Kiloliter: 1000 liters
    KILOLITER(1000.0),
    // Gallon (US): 3.78541 liters
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public String getMeasurementType() {
        return "VOLUME";
    }

    @Override
    public IMeasurable fromUnitName(String unitName) {
        if (unitName == null) return null;

        String normalized = unitName.trim().toUpperCase();

        // Accept both US/UK spellings and common short forms
        return switch (normalized) {
            case "MILLILITER", "MILLILITRE", "ML" -> MILLILITER;
            case "LITER", "LITRE", "L" -> LITER;
            case "KILOLITER", "KILOLITRE", "KL" -> KILOLITER;
            case "GALLON", "GAL" -> GALLON;
            default -> null;
        };
    }

    public static VolumeUnit from(String unitName) {
        IMeasurable measurable = LITER.fromUnitName(unitName);
        return measurable instanceof VolumeUnit volumeUnit ? volumeUnit : null;
    }
}