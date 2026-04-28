package com.cg.measurement.model;

public enum Unit {
    METER, CENTIMETER, KILOGRAM, GRAM, LITER, MILLILITER;

    public static Unit fromString(String text) {
        if (text != null) {
            for (Unit b : Unit.values()) {
                if (text.equalsIgnoreCase(b.name())) {
                    return b;
                }
            }
        }
        return null;
    }
}
