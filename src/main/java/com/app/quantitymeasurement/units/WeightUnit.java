package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

// Enum representing weight measurement units. Each enum value knows how to
// convert to/from a chosen base unit (kilogram) via a conversion factor.
public enum WeightUnit implements IMeasurable{

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    // convert a value in this unit to the base unit (kilogram)
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    // convert a value from the base unit into this unit
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
    
    public String getUnitName() {
    	return this.name();
    }

    @Override
    public String getMeasurementType() {
        return "WEIGHT";
    }

    @Override
    public IMeasurable fromUnitName(String unitName) {
        if (unitName == null) return null;
        try {
            return WeightUnit.valueOf(unitName.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null; // unknown unit name
        }
    }
}