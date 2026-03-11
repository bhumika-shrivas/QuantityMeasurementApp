package com.QuantityMeasurementApp.repository;

import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import java.util.List;

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
     * @param measurement the measurement entity to save; must not be null
     * @return the saved entity (may include generated fields like id or timestamp)
     */
    QuantityMeasurementEntity saveMeasurement(QuantityMeasurementEntity measurement);

    /**
     * Retrieve all persisted measurement records.
     *
     * @return list of measurements; never null (may be empty)
     */
    List<QuantityMeasurementEntity> getAllMeasurements();
}
