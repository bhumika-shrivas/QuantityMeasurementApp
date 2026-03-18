package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.IMeasurable;
import com.app.quantitymeasurement.Quantity;

/**
 * Internal model representation of a quantity used inside service layer.
 * Mirrors core behavior of Quantity but provides conversion helpers to/from Quantity.
 */
public final class QuantityModel<U extends IMeasurable> {

    private static final double EPSILON = 1e-5;

    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid numeric value");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> toQuantity() {
        return new Quantity<>(value, unit);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends IMeasurable> QuantityModel<T> fromQuantity(Quantity<?> q) {
        if (q == null) return null;
        return new QuantityModel<>((double) q.getValue(), (T) q.getUnit());
    }

    // Conversion to another unit
    public QuantityModel<U> convertTo(U targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double base = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new QuantityModel<>(converted, targetUnit);
    }

    // Arithmetic
    public QuantityModel<U> add(QuantityModel<U> other) {
        validateOperand(other);
        double baseThis = unit.convertToBaseUnit(value);
        double baseOther = other.unit.convertToBaseUnit(other.value);
        double baseResult = baseThis + baseOther;
        double converted = unit.convertFromBaseUnit(baseResult);
        return new QuantityModel<>(converted, unit);
    }

    public QuantityModel<U> subtract(QuantityModel<U> other) {
        validateOperand(other);
        double baseThis = unit.convertToBaseUnit(value);
        double baseOther = other.unit.convertToBaseUnit(other.value);
        double baseResult = baseThis - baseOther;
        double converted = unit.convertFromBaseUnit(baseResult);
        return new QuantityModel<>(converted, unit);
    }

    public double divide(QuantityModel<U> other) {
        if (other == null) throw new IllegalArgumentException("Null operand");
        // Validate categories match (prevent cross-category divide)
        validateOperand(other);
        double baseOther = other.unit.convertToBaseUnit(other.value);
        if (Math.abs(baseOther) < 1e-12) throw new ArithmeticException("Division by zero");
        double baseThis = unit.convertToBaseUnit(this.value);
        return baseThis / baseOther;
    }

    private void validateOperand(QuantityModel<U> other) {
        if (other == null) throw new IllegalArgumentException("Null operand");
        if (!getUnitCategory(this.unit).equals(getUnitCategory(other.unit)))
            throw new IllegalArgumentException("Cross-category operation not allowed");
    }

    private Object getUnitCategory(IMeasurable unit) {
        return (unit instanceof Enum<?> e) ? e.getDeclaringClass() : unit.getClass();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityModel<?> other)) return false;
        if (!getUnitCategory(this.unit).equals(getUnitCategory(other.unit))) return false;
        double baseThis = unit.convertToBaseUnit(this.value);
        double baseOther = other.unit.convertToBaseUnit(other.getValue());
        return Math.abs(baseThis - baseOther) <= EPSILON;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }
}