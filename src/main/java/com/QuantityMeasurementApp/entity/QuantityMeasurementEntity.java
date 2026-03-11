package com.QuantityMeasurementApp.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Entity representing a recorded quantity operation for persistence.
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operand1;
    private String operand2;
    private String operationType;
    private String result;
    private String errorMessage;
    private Instant timestamp;

    public QuantityMeasurementEntity() {
        // default constructor for serialization frameworks
        this.timestamp = Instant.now();
    }

    public QuantityMeasurementEntity(String operand1, String operand2, String operationType, String result, String errorMessage, Instant timestamp) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operationType = operationType;
        this.result = result;
        this.errorMessage = errorMessage;
        this.timestamp = (timestamp == null) ? Instant.now() : timestamp;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantityMeasurementEntity)) return false;
        QuantityMeasurementEntity that = (QuantityMeasurementEntity) o;
        return Objects.equals(operand1, that.operand1) &&
                Objects.equals(operand2, that.operand2) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(result, that.result) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand1, operand2, operationType, result, errorMessage, timestamp);
    }

    @Override
    public String toString() {
        return "QuantityMeasurementEntity{" +
                "operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                ", operationType='" + operationType + '\'' +
                ", result='" + result + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
