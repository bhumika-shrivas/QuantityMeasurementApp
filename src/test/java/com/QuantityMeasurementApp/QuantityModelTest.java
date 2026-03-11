package com.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.QuantityMeasurementApp.model.QuantityModel;
import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.WeightUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;

// Unit tests for QuantityModel: verify unit conversions, arithmetic operations,
// comparison/equality semantics, exception handling (e.g., division by zero),
// and interoperability with the lightweight Quantity DTO.
public class QuantityModelTest {

    private static final double EPSILON = 0.00001;

    @Test
    void conversionBetweenUnits() {
        // verify conversion between length units (feet -> inches)
        QuantityModel<LengthUnit> feet = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityModel<LengthUnit> inches = feet.convertTo(LengthUnit.INCHES);
        assertEquals(12.0, inches.getValue(), EPSILON);

        // verify conversion between volume units (litre -> millilitre)
        QuantityModel<VolumeUnit> litre = new QuantityModel<>(1.0, VolumeUnit.LITRE);
        QuantityModel<VolumeUnit> ml = litre.convertTo(VolumeUnit.MILLILITRE);
        assertEquals(1000.0, ml.getValue(), EPSILON);
    }

    @Test
    void arithmeticAddSubtract() {
        // addition and subtraction should handle unit conversion internally
        QuantityModel<LengthUnit> a = new QuantityModel<>(10.0, LengthUnit.FEET);
        QuantityModel<LengthUnit> b = new QuantityModel<>(6.0, LengthUnit.INCHES);

        QuantityModel<LengthUnit> sum = a.add(b);
        assertEquals(10.5, sum.getValue(), EPSILON);

        QuantityModel<LengthUnit> diff = a.subtract(b);
        assertEquals(9.5, diff.getValue(), EPSILON);
    }

    @Test
    void divisionAndEquality() {
        // dividing two compatible quantities yields a dimensionless ratio
        QuantityModel<LengthUnit> a = new QuantityModel<>(120.0, LengthUnit.INCHES);
        QuantityModel<LengthUnit> b = new QuantityModel<>(10.0, LengthUnit.FEET);

        double ratio = a.divide(b);
        assertEquals(1.0, ratio, EPSILON);

        // equality should consider unit conversions (120 inches == 10 feet)
        assertTrue(a.equals(b));
    }

    @Test
    void divisionByZeroThrows() {
        // dividing by a quantity with zero value should throw ArithmeticException
        QuantityModel<LengthUnit> a = new QuantityModel<>(10.0, LengthUnit.FEET);
        QuantityModel<LengthUnit> zero = new QuantityModel<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> a.divide(zero));
    }

    @Test
    void crossCategoryOperationsFail() {
        // operations between incompatible unit categories should throw IllegalArgumentException
        QuantityModel<LengthUnit> length = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityModel<WeightUnit> weight = new QuantityModel<>(1.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> length.add((QuantityModel) weight));
        assertThrows(IllegalArgumentException.class, () -> length.subtract((QuantityModel) weight));
        assertThrows(IllegalArgumentException.class, () -> length.divide((QuantityModel) weight));
    }

    @Test
    void temperatureEqualityAndConversion() {
        // temperature units have special conversions (C/F/K) and equality should work across them
        QuantityModel<TemperatureUnit> c = new QuantityModel<>(0.0, TemperatureUnit.CELSIUS);
        QuantityModel<TemperatureUnit> f = new QuantityModel<>(32.0, TemperatureUnit.FAHRENHEIT);
        QuantityModel<TemperatureUnit> k = new QuantityModel<>(273.15, TemperatureUnit.KELVIN);

        assertTrue(c.equals(f));
        assertTrue(c.equals(k));

        QuantityModel<TemperatureUnit> converted = f.convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, converted.getValue(), EPSILON);
    }

    @Test
    void interopWithQuantity() {
        // ensure QuantityModel can be created from Quantity DTO and converted back without losing data
        Quantity<LengthUnit> q = new Quantity<>(5.0, LengthUnit.FEET);
        QuantityModel<LengthUnit> model = QuantityModel.fromQuantity(q);
        assertEquals(q.getValue(), model.getValue(), EPSILON);
        assertEquals(q.getUnit(), model.getUnit());

        Quantity<?> back = model.toQuantity();
        assertEquals(q.getValue(), back.getValue(), EPSILON);
        assertEquals(q.getUnit(), back.getUnit());
    }
}