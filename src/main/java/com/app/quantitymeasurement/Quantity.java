package com.app.quantitymeasurement;

import java.util.function.BinaryOperator;

/**
 * Generic Quantity class supporting all measurement types with centralized arithmetic.
 */
public class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;

    private static final double EPSILON = 0.00001;

    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");

        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Invalid numeric value");

        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    // ==========================
    // EQUALITY
    // ==========================

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;

        // Prevent cross-category comparison
        if (!getUnitCategory(this.unit).equals(getUnitCategory(other.unit)))
            return false;

        double baseThis = unit.convertToBaseUnit(this.value);
        double baseOther = other.unit.convertToBaseUnit(other.getValue());

        return Math.abs(baseThis - baseOther) < EPSILON;
    }

    private Object getUnitCategory(IMeasurable unit) {
        return (unit instanceof Enum<?> e) ? e.getDeclaringClass() : unit.getClass();
    }

    @Override
    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }

    // ==========================
    // CONVERSION
    // ==========================

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");

        double baseValue = unit.convertToBaseUnit(this.value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(converted, targetUnit);
    }

    // ==========================
    // ADDITION
    // ==========================

    public Quantity<U> add(Quantity<U> other) {
        return performOperation(other, this.unit, Double::sum);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        return performOperation(other, targetUnit, Double::sum);
    }

    // ==========================
    // SUBTRACTION
    // ==========================

    public Quantity<U> subtract(Quantity<U> other) {
        return performOperation(other, this.unit, (a, b) -> a - b);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        return performOperation(other, targetUnit, (a, b) -> a - b);
    }

    // ==========================
    // DIVISION
    // ==========================

    public double divide(Quantity<U> other) {
        if (other == null)
            throw new IllegalArgumentException("Null operand");

        if (!getUnitCategory(this.unit).equals(getUnitCategory(other.unit)))
            throw new IllegalArgumentException("Cross-category operation not allowed");

        double baseOther = other.unit.convertToBaseUnit(other.value);

        if (Math.abs(baseOther) < EPSILON)
            throw new ArithmeticException("Division by zero");

        double baseThis = unit.convertToBaseUnit(this.value);

        return baseThis / baseOther;
    }

    // ==========================
    // CENTRALIZED ARITHMETIC (UC13)
    // ==========================

    private Quantity<U> performOperation(
            Quantity<U> other,
            U targetUnit,
            BinaryOperator<Double> operator) {

        validateOperands(other, targetUnit);

        double baseThis = unit.convertToBaseUnit(this.value);
        double baseOther = other.unit.convertToBaseUnit(other.value);

        double baseResult = operator.apply(baseThis, baseOther);

        double converted = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(converted, targetUnit);
    }

    private void validateOperands(Quantity<U> other, U targetUnit) {
        if (other == null)
            throw new IllegalArgumentException("Null operand");

        if (targetUnit == null)
            throw new IllegalArgumentException("Null target unit");

        if (!getUnitCategory(this.unit).equals(getUnitCategory(other.unit)))
            throw new IllegalArgumentException("Cross-category operation not allowed");
    }
}