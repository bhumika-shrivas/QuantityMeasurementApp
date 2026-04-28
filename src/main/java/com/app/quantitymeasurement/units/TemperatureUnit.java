package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

/**
 * TemperatureUnit defines temperature units and handles affine conversions
 * to/from a canonical base unit (Celsius).
 *
 * Temperature conversion differs from other units because transformations
 * are affine (scale + offset) rather than purely multiplicative. Each enum
 * constant overrides conversion methods accordingly.
 */
public enum TemperatureUnit implements IMeasurable {

    CELSIUS {
        @Override
        public double convertToBaseUnit(double value) {
            return value;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue;
        }

        @Override
        public double getConversionFactor() {
            return 1.0;
        }

        @Override
        public String getUnitName() {
            return name();
        }
    },

    FAHRENHEIT {
        @Override
        public double convertToBaseUnit(double value) {
            // Convert Fahrenheit to Celsius: (F - 32) * 5/9
            return (value - 32) * 5.0 / 9.0;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            // Convert Celsius to Fahrenheit: (C * 9/5) + 32
            return (baseValue * 9.0 / 5.0) + 32;
        }

        @Override
        public double getConversionFactor() {
            return 1.0;
        }

        @Override
        public String getUnitName() {
            return name();
        }
    },

    KELVIN {
        @Override
        public double convertToBaseUnit(double value) {
            // Kelvin to Celsius
            return value - 273.15;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            // Celsius to Kelvin
            return baseValue + 273.15;
        }

        @Override
        public double getConversionFactor() {
            return 1.0;
        }

        @Override
        public String getUnitName() {
            return name();
        }
    };

    // Implement missing IMeasurable methods so the enum compiles cleanly
    @Override
    public String getMeasurementType() {
        return "TEMPERATURE";
    }

    @Override
    public IMeasurable fromUnitName(String unitName) {
        if (unitName == null) return null;
        try {
            return TemperatureUnit.valueOf(unitName.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null; // unknown temperature unit
        }
    }
}