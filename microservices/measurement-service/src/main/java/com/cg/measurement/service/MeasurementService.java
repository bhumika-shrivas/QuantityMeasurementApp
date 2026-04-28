package com.cg.measurement.service;

import com.cg.measurement.model.MeasurementRequest;
import com.cg.measurement.model.MeasurementResponse;
import com.cg.measurement.model.Unit;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    public MeasurementResponse calculate(MeasurementRequest request) {
        Unit u1 = Unit.fromString(request.getUnit1());
        Unit u2 = Unit.fromString(request.getUnit2());

        // Validate unit safely
        if (u1 == null) {
            throw new IllegalArgumentException("Invalid unit1 provided: " + request.getUnit1());
        }

        // For convert or if unit2 is provided
        if (request.getUnit2() != null && !request.getUnit2().isEmpty() && u2 == null) {
            throw new IllegalArgumentException("Invalid unit2 provided: " + request.getUnit2());
        }

        double val1 = request.getValue1();
        double val2 = request.getValue2();
        String op = request.getOperation() != null ? request.getOperation().toLowerCase() : "";

        double result = 0;
        switch (op) {
            case "add":
                result = add(val1, val2);
                break;
            case "subtract":
                result = subtract(val1, val2);
                break;
            case "multiply":
                result = multiply(val1, val2);
                break;
            case "divide":
                result = divide(val1, val2);
                break;
            case "convert":
                // For now, doing simple operations/passthrough (no complex conversions yet)
                result = convert(val1, u1, u2);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + op);
        }

        return new MeasurementResponse(result, u1.name());
    }

    public double add(double v1, double v2) {
        return v1 + v2;
    }

    public double subtract(double v1, double v2) {
        return v1 - v2;
    }

    public double multiply(double v1, double v2) {
        return v1 * v2;
    }

    public double divide(double v1, double v2) {
        if (v2 == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return v1 / v2;
    }

    public double convert(double v1, Unit from, Unit to) {
        // Placeholder for simple conversion returning the same value for now
        return v1;
    }
}
