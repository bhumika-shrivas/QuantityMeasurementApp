package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

// Enum representing volume measurement units. Uses litre as the base unit.
public enum VolumeUnit implements IMeasurable {

    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;

    VolumeUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    // Convert a value from the unit to the base unit (litre).
    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    // Convert a value from the base unit (litre) to the unit.
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
        try {
            return VolumeUnit.valueOf(unitName.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null; // unknown unit
        }
    }
}