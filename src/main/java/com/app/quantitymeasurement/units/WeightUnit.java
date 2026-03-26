package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

/**
 * WeightUnit defines supported weight measurement units and conversion logic.
 *
 * Role in the application:
 * - Serves as a typed representation of weight units (KILOGRAM, GRAM, POUND).
 * - Provides conversion to/from a canonical base unit (kilogram) using a
 *   conversion factor. The rest of the application can rely on the base unit
 *   for comparisons and arithmetic.
 *
 * Design notes:
 * - Conversion is implemented using a multiplicative conversion factor
 *   relative to the base unit (kilogram).
 * - Implements IMeasurable so callers can treat different unit enums
 *   polymorphically.
 */
public enum WeightUnit implements IMeasurable{

    // Base unit: kilogram (factor 1.0)
    KILOGRAM(1.0),
    // Gram: 0.001 kilograms
    GRAM(0.001),
    // Pound: 0.453592 kilograms
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // Returns the factor used to convert this unit to the base unit
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
    
    // Human-readable unit name (matches enum constant name)
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