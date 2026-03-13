package com.app.quantitymeasurement.repository;

import java.util.List;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

/**
 * Repository interface that abstracts persistence of quantity measurement records.
 * Implementations may store records in memory, files, databases, or remote services.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Persist a measurement record.
     * Implementations should handle null checks and throw appropriate runtime exceptions
     * for unrecoverable storage errors.
     *
     * @param entity the measurement entity to save; must not be null
     * @return the saved entity (may include generated fields like id or timestamp)
     */
    QuantityMeasurementEntity saveMeasurement(QuantityMeasurementEntity entity);

    /**
     * Retrieve all persisted measurement records.
     *
     * @return list of measurements; never null (may be empty)
     */
    List<QuantityMeasurementEntity> getAllMeasurements();
}