package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

public enum LengthUnit implements IMeasurable{

    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
    
    public String getUnitName() {
    	return this.name();
    }

    // Implement measurement type for this enum
    @Override
    public String getMeasurementType() {
        return "LENGTH";
    }

    // Resolve a unit by its name (case-insensitive). Returns null if not found.
    @Override
    public IMeasurable fromUnitName(String unitName) {
        if (unitName == null) return null;
        try {
            return LengthUnit.valueOf(unitName.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}