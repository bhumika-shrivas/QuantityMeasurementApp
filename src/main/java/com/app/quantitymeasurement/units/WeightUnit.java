package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

public enum WeightUnit implements IMeasurable {

    // Base unit: kilogram
    KILOGRAM(1.0),
    // Gram: 0.001 kilograms
    GRAM(0.001),
    // Tonne: 1000 kilograms
    TONNE(1000.0),
    // Keep POUND as alias-supported unit if needed
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
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
        return this.name();
    }

    @Override
    public String getMeasurementType() {
        return "WEIGHT";
    }

    @Override
    public IMeasurable fromUnitName(String unitName) {
        if (unitName == null) return null;

        String normalized = unitName.trim().toUpperCase();

        return switch (normalized) {
            case "KILOGRAM", "KG" -> KILOGRAM;
            case "GRAM", "G" -> GRAM;
            case "TONNE", "TON" -> TONNE;
            case "POUND", "LB", "LBS" -> POUND;
            default -> null;
        };
    }
}