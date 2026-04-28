package com.cg.measurement.model;

public class MeasurementResponse {
    private double result;
    private String unit;

    public MeasurementResponse() {
    }

    public MeasurementResponse(double result, String unit) {
        this.result = result;
        this.unit = unit;
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
}
