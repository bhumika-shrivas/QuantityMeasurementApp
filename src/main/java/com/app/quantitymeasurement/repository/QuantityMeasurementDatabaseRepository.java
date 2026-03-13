package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC repository implementation using a simple ConnectionPool utility.
 * Implements the `IQuantityMeasurementRepository` contract exactly.
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final String INSERT_SQL =
            "INSERT INTO quantity_measurements (operand1, operand2, operationType, result, errorMessage, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
            "SELECT operand1, operand2, operationType, result, errorMessage, timestamp FROM quantity_measurements";

    @Override
    public QuantityMeasurementEntity saveMeasurement(QuantityMeasurementEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }

        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            // Use try-with-resources for PreparedStatement to ensure it's closed
            try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {

                statement.setString(1, entity.getOperand1());
                statement.setString(2, entity.getOperand2());
                statement.setString(3, entity.getOperationType());
                statement.setString(4, entity.getResult());
                statement.setString(5, entity.getErrorMessage());

                Timestamp ts = (entity.getTimestamp() == null) ? new Timestamp(System.currentTimeMillis()) : Timestamp.from(entity.getTimestamp());
                statement.setTimestamp(6, ts);

                statement.executeUpdate();

                return entity;
            }

        } catch (Exception e) {
            throw new DatabaseException("Error saving measurement", e);
        } finally {
            ConnectionPool.releaseConnection(connection);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {

        List<QuantityMeasurementEntity> measurements = new ArrayList<>();
        Connection connection = null;

        try {
            connection = ConnectionPool.getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    QuantityMeasurementEntity entity = new QuantityMeasurementEntity();

                    entity.setOperand1(resultSet.getString("operand1"));
                    entity.setOperand2(resultSet.getString("operand2"));
                    entity.setOperationType(resultSet.getString("operationType"));
                    entity.setResult(resultSet.getString("result"));
                    entity.setErrorMessage(resultSet.getString("errorMessage"));
                    java.sql.Timestamp t = resultSet.getTimestamp("timestamp");
                    if (t != null) {
                        entity.setTimestamp(t.toInstant());
                    }

                    measurements.add(entity);
                }
            }

        } catch (Exception e) {
            throw new DatabaseException("Error retrieving measurements", e);
        } finally {
            ConnectionPool.releaseConnection(connection);
        }

        return measurements;
    }
}