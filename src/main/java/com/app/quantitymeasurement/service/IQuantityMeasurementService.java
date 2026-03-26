package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import java.util.List;

/**
 * IQuantityMeasurementService defines the contract for operations that
 * compare, convert and perform arithmetic on quantities expressed in
 * measurement units (length, weight, volume, temperature, etc.).
 *
 * Each method returns a QuantityMeasurementDTO which includes the result,
 * the units involved, and metadata (for history or error reporting).
 * Implementations should normalize input values to a canonical base unit
 * before performing comparisons or arithmetic to ensure correctness.
 *
 * Error behavior:
 * - If units are incompatible (e.g. LENGTH vs WEIGHT) implementations should
 *   return a DTO indicating an error (or throw a domain-specific exception)
 *   depending on the project's error-handling convention.
 */
public interface IQuantityMeasurementService {

    // Compare two quantities (returns result and whether they are equal)
    QuantityMeasurementDTO compare(QuantityDTO left, QuantityDTO right);

    // Convert a source quantity to the target unit name
    QuantityMeasurementDTO convert(QuantityDTO source, String targetUnit);

    // Add two quantities (must be of the same measurement type)
    QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b);

    // Subtract b from a
    QuantityMeasurementDTO subtract(QuantityDTO a, QuantityDTO b);

    // Divide a by b (returns error or special value on divide-by-zero)
    QuantityMeasurementDTO divide(QuantityDTO a, QuantityDTO b);

    // History and analytics helpers: filter past operations by operation type
    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);
    List<QuantityMeasurementDTO> getErrorHistory();
    long getCountByOperation(String operation);
}