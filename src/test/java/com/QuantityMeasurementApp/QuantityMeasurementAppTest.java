package com.QuantityMeasurementApp;

import org.junit.jupiter.api.Test;

import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.WeightUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive Test Suite for Quantity Measurement Application.
 * Covers Length, Weight, and Volume categories using generic Quantity class.
 */
public class QuantityMeasurementAppTest {

    private static final double EPSILON1 = 0.001;

    // --- Subtraction Tests ---

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        // Verifies that 10.0 FEET - 5.0 FEET = 5.0 FEET.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(5.0, result.getValue(), EPSILON1);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        // Verifies that 10.0 LITRE - 3.0 LITRE = 7.0 LITRE.
        Quantity<VolumeUnit> q1 = new Quantity<>(10.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(3.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> result = q1.subtract(q2);
        assertEquals(7.0, result.getValue(), EPSILON1);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        // Verifies that 10.0 FEET - 6.0 INCHES = 9.5 FEET.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(9.5, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        // Verifies that 120.0 INCHES - 5.0 FEET = 60.0 INCHES.
        Quantity<LengthUnit> q1 = new Quantity<>(120.0, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(60.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        // Verifies explicit target unit specification in feet.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2, LengthUnit.FEET);
        assertEquals(9.5, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        // Verifies explicit target unit specification in inches.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2, LengthUnit.INCHES);
        assertEquals(114.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        // Verifies explicit target unit specification in millilitre.
        Quantity<VolumeUnit> q1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(2.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> result = q1.subtract(q2, VolumeUnit.MILLILITRE);
        assertEquals(3000.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        // Verifies subtraction resulting in negative values.
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(-5.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_ResultingInZero() {
        // Verifies subtraction resulting in zero.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(120.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(0.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        // Verifies identity element property.
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(5.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        // Verifies subtraction with negative operands.
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(-2.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(7.0, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_NonCommutative() {
        // Verifies that A.subtract(B) != B.subtract(A).
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertNotEquals(q1.subtract(q2).getValue(), q2.subtract(q1).getValue());
    }

    @Test
    void testSubtraction_WithLargeValues() {
        // Verifies large magnitude subtraction.
        Quantity<WeightUnit> q1 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(5e5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> result = q1.subtract(q2);
        assertEquals(5e5, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_WithSmallValues() {
        // Verifies small magnitude subtraction with precision.
        Quantity<LengthUnit> q1 = new Quantity<>(0.001, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0005, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(0.0005, result.getValue(), EPSILON1);
    }

    @Test
    void testSubtraction_NullOperand() {
        // Verifies null operand validation.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(null));
    }

    @Test
    void testSubtraction_CrossCategory() {
        // Verifies cross-category prevention.
        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        // The generic type system prevents this at compile time if types are mismatched.
        // We use raw types to bypass compile-time checks for this test.
        assertThrows(IllegalArgumentException.class, () -> ((Quantity)length).subtract(weight));
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        // Verifies subtraction works across length, weight, and volume.
        assertDoesNotThrow(() -> {
            new Quantity<>(1.0, LengthUnit.FEET).subtract(new Quantity<>(1.0, LengthUnit.INCHES));
            new Quantity<>(1.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(100.0, WeightUnit.GRAM));
            new Quantity<>(1.0, VolumeUnit.LITRE).subtract(new Quantity<>(100.0, VolumeUnit.MILLILITRE));
        });
    }

    @Test
    void testSubtraction_ChainedOperations() {
        // Verifies method chaining support.
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(7.0, result.getValue(), EPSILON1);
    }

    // --- Division Tests ---

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        // Verifies 10.0 FEET / 2.0 FEET = 5.0.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        double result = q1.divide(q2);
        assertEquals(5.0, result, EPSILON1);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        // Verifies 10.0 LITRE / 5.0 LITRE = 2.0.
        Quantity<VolumeUnit> q1 = new Quantity<>(10.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(5.0, VolumeUnit.LITRE);
        double result = q1.divide(q2);
        assertEquals(2.0, result, EPSILON1);
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        // Verifies 24.0 INCHES / 2.0 FEET = 1.0.
        Quantity<LengthUnit> q1 = new Quantity<>(24.0, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        double result = q1.divide(q2);
        assertEquals(1.0, result, EPSILON1);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        // Verifies 2.0 KILOGRAM / 2000.0 GRAM = 1.0.
        Quantity<WeightUnit> q1 = new Quantity<>(2.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(2000.0, WeightUnit.GRAM);
        double result = q1.divide(q2);
        assertEquals(1.0, result, EPSILON1);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        // Ratio > 1.0 case.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        assertTrue(q1.divide(q2) > 1.0);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        // Ratio < 1.0 case.
        Quantity<LengthUnit> q1 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(10.0, LengthUnit.FEET);
        assertEquals(0.5, q1.divide(q2), EPSILON1);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        // Equivalence detection through division.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(120.0, LengthUnit.INCHES);
        assertEquals(1.0, q1.divide(q2), EPSILON1);
    }

    @Test
    void testDivision_NonCommutative() {
        // Verifies that A.divide(B) != B.divide(A).
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertNotEquals(q1.divide(q2), q2.divide(q1), EPSILON1);
    }

    @Test
    void testDivision_ByZero() {
        // Division by zero prevention.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }

    @Test
    void testDivision_WithLargeRatio() {
        // Very large ratios.
        Quantity<WeightUnit> q1 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertEquals(1e6, q1.divide(q2), EPSILON1);
    }

    @Test
    void testDivision_WithSmallRatio() {
        // Very small ratios and precision.
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        assertEquals(1e-6, q1.divide(q2), 1e-12);
    }

    @Test
    void testDivision_NullOperand() {
        // Null operand validation.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> q1.divide(null));
    }

    @Test
    void testDivision_CrossCategory() {
        // Cross-category prevention.
        Quantity<LengthUnit> length = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> ((Quantity)length).divide(weight));
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        // Scalability across categories.
        assertDoesNotThrow(() -> {
            new Quantity<>(1.0, LengthUnit.FEET).divide(new Quantity<>(1.0, LengthUnit.FEET));
            new Quantity<>(1.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM));
            new Quantity<>(1.0, VolumeUnit.LITRE).divide(new Quantity<>(1.0, VolumeUnit.LITRE));
        });
    }

    @Test
    void testDivision_Associativity() {
        // Verifies that (A ÷ B) ÷ C != A ÷ (B ÷ C) (non-associative).
        Quantity<LengthUnit> a = new Quantity<>(20.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(2.0, LengthUnit.FEET);

        double left = (a.divide(b)) / c.getValue();
        double right = a.getValue() / (b.divide(c));
        assertNotEquals(left, right, EPSILON1);
    }

    @Test
    void testSubtractionAndDivision_Integration() {
        // verifies that subtraction and division operations coexist.
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET));
        assertEquals(4.0, result.divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON1);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        // Verifies that A.add(B).subtract(B) equals A.
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = a.add(b).subtract(b);
        assertTrue(a.equals(result));
    }

    @Test
    void testSubtraction_Immutability() {
        // Verifies that original quantities are unchanged.
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        a.subtract(b);
        assertEquals(10.0, a.getValue());
    }

    @Test
    void testDivision_Immutability() {
        // Verifies that original quantities are unchanged.
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue());
    }

    @Test
    void testSubtraction_PrecisionAndRounding() {
        // Verifies subtraction results with precision.
        Quantity<LengthUnit> q1 = new Quantity<>(10.555, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.555, LengthUnit.FEET);
        Quantity<LengthUnit> result = q1.subtract(q2);
        assertEquals(10.0, result.getValue(), EPSILON1);
    }

    @Test
    void testDivision_PrecisionHandling() {
        // Verifies dimensionless result handling.
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(3.3333, q1.divide(q2), 0.001);
    }

    // --- Volume Equality and Conversion Tests ---

    @Test
    void testEquality_LitreToLitre_SameValue() {
        // Verifies that equals() returns true for identical litre measurements.
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(q1.equals(q2));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        // Verifies that equals() returns false for different litre measurements.
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(2.0, VolumeUnit.LITRE);
        assertFalse(q1.equals(q2));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        // Verifies that equals() returns true for litre-to-millilitre equivalence.
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(litre.equals(ml));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        // Verifies that equals() returns true (tests symmetry of conversion).
        Quantity<VolumeUnit> millilitre = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(millilitre.equals(litre));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        // Verifies that equals() returns true for litre-to-gallon equivalence.
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> gallon = new Quantity<>(0.264172, VolumeUnit.GALLON);
        assertTrue(litre.equals(gallon));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        // Verifies that equals() returns true (tests symmetry of conversion).
        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litre = new Quantity<>(3.78541, VolumeUnit.LITRE);
        assertTrue(gallon.equals(litre));
    }

    @Test
    void testConversion_LitreToMillilitre() {
        // Verifies conversion from litre to millilitre.
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals(1000.0, q.convertTo(VolumeUnit.MILLILITRE).getValue(), EPSILON1);
    }

    @Test
    void testConversion_GallonToLitre() {
        // Verifies conversion from gallon to litre.
        Quantity<VolumeUnit> q = new Quantity<>(1.0, VolumeUnit.GALLON);
        assertEquals(3.785, q.convertTo(VolumeUnit.LITRE).getValue(), 0.01);
    }

    // --- Basic Equality Tests (Length & Weight) ---

    @Test
    void testEquality_FeetToInches() {
        assertTrue(new Quantity<>(1.0, LengthUnit.FEET).equals(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testEquality_KilogramToGram() {
        assertTrue(new Quantity<>(1.0, WeightUnit.KILOGRAM).equals(new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        // Verifies that equals() returns false when comparing incompatible measurement categories.
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        assertFalse(volume.equals(length));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        // Verifies that equals() returns false when comparing volume with weight.
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(volume.equals(weight));
    }

    @Test
    void testEquality_NullComparison() {
        // Verifies that equals() returns false when comparing with null.
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertFalse(volume.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        // Verifies that a volume object equals itself (reflexive property).
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertTrue(volume.equals(volume));
    }

    @Test
    void testEquality_NullUnit() {
        // Verifies that Exception thrown for null unit in constructor.
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }
    

    @Test
    void testCelsiusToFahrenheitEquality() {
        assertTrue(
            new Quantity<>(0.0, TemperatureUnit.CELSIUS)
                .equals(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT))
        );
    }

    @Test
    void testCelsiusToKelvinEquality() {
        assertTrue(
            new Quantity<>(0.0, TemperatureUnit.CELSIUS)
                .equals(new Quantity<>(273.15, TemperatureUnit.KELVIN))
        );
    }

    @Test
    void testFahrenheitToKelvinEquality() {
        assertTrue(
            new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)
                .equals(new Quantity<>(273.15, TemperatureUnit.KELVIN))
        );
    }

    @Test
    void testTemperatureAddition() {
        Quantity<TemperatureUnit> result =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS)
                        .add(new Quantity<>(10.0, TemperatureUnit.CELSIUS));

        assertEquals(10.0, result.getValue(), EPSILON1);
    }

    @Test
    void testTemperatureSubtraction() {
        Quantity<TemperatureUnit> result =
                new Quantity<>(10.0, TemperatureUnit.CELSIUS)
                        .subtract(new Quantity<>(5.0, TemperatureUnit.CELSIUS));

        assertEquals(5.0, result.getValue(), EPSILON1);
    }

    @Test
    void testCrossCategoryComparison() {
        assertFalse(
            new Quantity<>(0.0, TemperatureUnit.CELSIUS)
                .equals(new Quantity<>(0.0, LengthUnit.FEET))
        );
    }
}