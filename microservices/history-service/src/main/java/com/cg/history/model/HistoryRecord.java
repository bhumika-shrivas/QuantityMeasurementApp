package com.cg.history.model;

import java.time.LocalDateTime;

public class HistoryRecord {
    private Long id;
    private String operation;
    private double input1;
    private double input2;
    private double result;
    private String unit;
    private LocalDateTime timestamp;

    public HistoryRecord() {
    }

    public HistoryRecord(Long id, String operation, double input1, double input2, double result, String unit, LocalDateTime timestamp) {
        this.id = id;
        this.operation = operation;
        this.input1 = input1;
        this.input2 = input2;
        this.result = result;
        this.unit = unit;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getInput1() {
        return input1;
    }

    public void setInput1(double input1) {
        this.input1 = input1;
    }

    public double getInput2() {
        return input2;
    }

    public void setInput2(double input2) {
        this.input2 = input2;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
