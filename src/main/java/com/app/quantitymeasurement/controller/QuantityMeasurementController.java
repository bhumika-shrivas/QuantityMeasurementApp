package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.impl.QuantityMeasurementServiceImpl;

/**
 * Thin controller layer that validates incoming DTOs and delegates to service implementation.
 * This class contains no business logic and is suitable to be adapted into a REST controller later.
 */

public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    public QuantityMeasurementController() {
        this.service = new QuantityMeasurementServiceImpl();
    }

    // Allow injection for tests
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    public String performComparison(QuantityDTO left, QuantityDTO right) {
        validateDto(left);
        validateDto(right);
        QuantityDTO res = service.compare(left, right);
        return formatResult(res);
    }

    public String performConversion(QuantityDTO source, String targetUnit) {
        validateDto(source);
        if (targetUnit == null || targetUnit.trim().isEmpty())
            throw QuantityMeasurementException.invalidConversion("targetUnit required");
        QuantityDTO res = service.convert(source, targetUnit);
        return formatResult(res);
    }

    public String performAddition(QuantityDTO a, QuantityDTO b) {
        validateDto(a);
        validateDto(b);
        QuantityDTO res = service.add(a, b);
        return formatResult(res);
    }

    public String performSubtraction(QuantityDTO a, QuantityDTO b) {
        validateDto(a);
        validateDto(b);
        QuantityDTO res = service.subtract(a, b);
        return formatResult(res);
    }

    public String performDivision(QuantityDTO a, QuantityDTO b) {
        validateDto(a);
        validateDto(b);
        QuantityDTO res = service.divide(a, b);
        return formatResult(res);
    }

    private void validateDto(QuantityDTO dto) {
        if (dto == null) throw QuantityMeasurementException.invalidConversion("QuantityDTO is null");
        if (dto.getMeasurementType() == null || dto.getMeasurementType().trim().isEmpty())
            throw QuantityMeasurementException.invalidConversion("measurementType required");
        if (dto.getUnit() == null || dto.getUnit().trim().isEmpty())
            throw QuantityMeasurementException.invalidConversion("unit required");
    }

    private String formatResult(QuantityDTO dto) {
        if (dto == null) return "null";
        return String.format("Result: value=%s, unit=%s, type=%s", dto.getValue(), dto.getUnit(), dto.getMeasurementType());
    }
}
