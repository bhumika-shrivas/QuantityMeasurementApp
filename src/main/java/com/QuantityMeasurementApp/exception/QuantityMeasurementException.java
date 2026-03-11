package com.QuantityMeasurementApp.exception;

/**
 * Centralized runtime exception for quantity measurement domain errors.
 * Covers invalid conversions, cross-category operations, arithmetic errors, etc.
 */
public class QuantityMeasurementException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QuantityMeasurementException() {
        super();
    }

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityMeasurementException(Throwable cause) {
        super(cause);
    }

    // Convenience factory methods for common error cases

    public static QuantityMeasurementException invalidConversion(String details) {
        return new QuantityMeasurementException("Invalid conversion: " + details);
    }

    public static QuantityMeasurementException crossCategoryOperation(String details) {
        return new QuantityMeasurementException("Cross-category operation not allowed: " + details);
    }

    public static QuantityMeasurementException arithmeticError(String details) {
        return new QuantityMeasurementException("Arithmetic error: " + details);
    }
}
