package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;

/**
 * Service interface for Quantity Measurement domain operations.
 * All methods accept and return DTOs; business logic resides in implementations.
 */
public interface IQuantityMeasurementService {

    /**
     * Compare two quantities. Implementations should handle unit conversions if necessary.
     * @param left left operand
     * @param right right operand
     * @return a QuantityDTO representing the comparison result (interpretation is implementation-specific). 
     */
    QuantityDTO compare(QuantityDTO left, QuantityDTO right);

    /**
     * Convert a quantity to the target unit.
     * @param source the quantity to convert
     * @param targetUnit the desired unit string
     * @return converted QuantityDTO with target unit and computed value
     */
    QuantityDTO convert(QuantityDTO source, String targetUnit);

    /**
     * Add two quantities (with conversion when necessary).
     * @param a first addend
     * @param b second addend
     * @return QuantityDTO containing the sum
     */
    QuantityDTO add(QuantityDTO a, QuantityDTO b);

    /**
     * Subtract two quantities (with conversion when necessary): a - b
     * @param a minuend
     * @param b subtrahend
     * @return QuantityDTO containing the difference
     */
    QuantityDTO subtract(QuantityDTO a, QuantityDTO b);

    /**
     * Divide two quantities (with conversion when necessary): a / b
     * @param a dividend
     * @param b divisor
     * @return QuantityDTO containing the division result
     */
    QuantityDTO divide(QuantityDTO a, QuantityDTO b);
}
