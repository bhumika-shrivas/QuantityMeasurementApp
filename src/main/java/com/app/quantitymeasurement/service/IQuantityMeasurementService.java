package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO compare(QuantityDTO left, QuantityDTO right);
    QuantityMeasurementDTO convert(QuantityDTO source, String targetUnit);
    QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b);
    QuantityMeasurementDTO subtract(QuantityDTO a, QuantityDTO b);
    QuantityMeasurementDTO divide(QuantityDTO a, QuantityDTO b);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);
    List<QuantityMeasurementDTO> getErrorHistory();
    long getCountByOperation(String operation);
}