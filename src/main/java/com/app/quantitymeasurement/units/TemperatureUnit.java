package com.app.quantitymeasurement.units;

import com.app.quantitymeasurement.IMeasurable;

/**
 * Enum representing Temperature Units.
 * Base unit: CELSIUS
 * Temperature conversions are affine (offset + scale) rather than pure scaling,
 * so each unit provides custom convertToBaseUnit/convertFromBaseUnit implementations.
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
            return (value - 32) * 5.0 / 9.0;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
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
            return value - 273.15;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
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