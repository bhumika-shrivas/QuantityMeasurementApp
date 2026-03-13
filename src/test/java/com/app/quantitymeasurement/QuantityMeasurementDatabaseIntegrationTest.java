package com.app.quantitymeasurement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementDatabaseIntegrationTest {

    private IQuantityMeasurementRepository repository;

    @BeforeEach
    void setup() throws Exception {
        // Initialize repository (this triggers ConnectionPool/Hikari setup and schema creation)
        repository = new QuantityMeasurementDatabaseRepository();

        // Clean the table so test runs deterministically
        try (Connection c = ConnectionPool.getConnection(); Statement s = c.createStatement()) {
            s.executeUpdate("DELETE FROM quantity_measurements");
        }
    }

    @AfterEach
    void teardown() {
        // Shutdown the Hikari datasource to free resources between test runs
        try {
            ConnectionPool.shutdown();
        } catch (Exception ignored) {
        }
    }

    @Test
    void testSaveAndReadMeasurement() throws Exception {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity("A","B","ADD","1.0","", Instant.now());
        repository.saveMeasurement(entity);

        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertFalse(all.isEmpty(), "Expected at least one measurement in DB");

        boolean found = all.stream().anyMatch(e ->
                "A".equals(e.getOperand1()) &&
                "B".equals(e.getOperand2()) &&
                "ADD".equals(e.getOperationType()) &&
                "1.0".equals(e.getResult())
        );

        assertTrue(found, "Saved measurement should be retrievable from database");
    }
}
