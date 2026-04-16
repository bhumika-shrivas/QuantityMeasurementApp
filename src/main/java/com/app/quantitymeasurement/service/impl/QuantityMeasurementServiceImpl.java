package com.app.quantitymeasurement.service.impl;

import com.app.quantitymeasurement.IMeasurable;
import com.app.quantitymeasurement.Quantity;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.units.LengthUnit;
import com.app.quantitymeasurement.units.TemperatureUnit;
import com.app.quantitymeasurement.units.VolumeUnit;
import com.app.quantitymeasurement.units.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {
	private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);

	@Autowired
	private QuantityMeasurementRepository repository;

	@Override
	public QuantityMeasurementDTO compare(QuantityDTO left, QuantityDTO right) {
	    QuantityMeasurementEntity entity = buildEntity(left, right, "COMPARE");
	    try {
	        Quantity<?> qLeft = convertDtoToModel(left);
	        Quantity<?> qRight = convertDtoToModel(right);
	        boolean result = qLeft.equals(qRight);
	        entity.setResultValue(String.valueOf(result));
	        log.info("COMPARE {} {} == {} {} -> {}", left.getValue(), left.getUnit(),
	                right.getValue(), right.getUnit(), result);
	    } catch (Exception ex) {
	        handleError(entity, "compare Error: " + ex.getMessage());
	    }
	    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public QuantityMeasurementDTO convert(QuantityDTO source, String targetUnit) {
	    QuantityMeasurementEntity entity = buildEntity(source, null, "CONVERT");
	    entity.setThatUnit(targetUnit);
	    try {
	        Quantity q = convertDtoToModel(source);
	        IMeasurable target = resolveUnit(source.getMeasurementType(), targetUnit);
	        Quantity<?> converted = q.convertTo(target);
	        entity.setResultValue(String.valueOf(converted.getValue()));
	        entity.setResultUnit(target.getUnitName());
	    } catch (Exception ex) {
	        handleError(entity, "convert Error: " + ex.getMessage());
	    }
	    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public QuantityMeasurementDTO add(QuantityDTO a, QuantityDTO b) {
	    QuantityMeasurementEntity entity = buildEntity(a, b, "ADD");
	    try {
	        Quantity qa = convertDtoToModel(a);
	        Quantity qb = convertDtoToModel(b);
	        Quantity<?> result = qa.add(qb);
	        entity.setResultValue(String.valueOf(result.getValue()));
	        entity.setResultUnit(result.getUnit().getUnitName());
	    } catch (Exception ex) {
	        handleError(entity, "add Error: " + ex.getMessage());
	    }
	    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public QuantityMeasurementDTO subtract(QuantityDTO a, QuantityDTO b) {
	    QuantityMeasurementEntity entity = buildEntity(a, b, "SUBTRACT");
	    try {
	        Quantity qa = convertDtoToModel(a);
	        Quantity qb = convertDtoToModel(b);
	        Quantity<?> result = qa.subtract(qb);
	        entity.setResultValue(String.valueOf(result.getValue()));
	        entity.setResultUnit(result.getUnit().getUnitName());
	    } catch (Exception ex) {
	        handleError(entity, "subtract Error: " + ex.getMessage());
	    }
	    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public QuantityMeasurementDTO divide(QuantityDTO a, QuantityDTO b) {
	    QuantityMeasurementEntity entity = buildEntity(a, b, "DIVIDE");
	    try {
	        Quantity qa = convertDtoToModel(a);
	        Quantity qb = convertDtoToModel(b);
	        double result = qa.divide(qb);
	        entity.setResultValue(String.valueOf(result));
	        entity.setResultUnit("RATIO");
	    } catch (Exception ex) {
	        handleError(entity, "divide Error: " + ex.getMessage());
	    }
	    return QuantityMeasurementDTO.fromEntity(repository.save(entity));
	}

	@Override
	public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
	    return QuantityMeasurementDTO.fromEntityList(repository.findByOperation(operation));
	}

	@Override
	public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String type) {
	    return QuantityMeasurementDTO.fromEntityList(repository.findByThisMeasurementType(type));
	}

	@Override
	public List<QuantityMeasurementDTO> getErrorHistory() {
	    return QuantityMeasurementDTO.fromEntityList(repository.findByIsErrorTrue());
	}

	@Override
	public long getCountByOperation(String operation) {
	    return repository.countByOperationAndIsErrorFalse(operation);
	}

	private QuantityMeasurementEntity buildEntity(QuantityDTO a, QuantityDTO b, String op) {
	    QuantityMeasurementEntity e = new QuantityMeasurementEntity();
	    if (a != null) {
	        e.setThisValue(a.getValue());
	        e.setThisUnit(a.getUnit());
	        e.setThisMeasurementType(a.getMeasurementType());
	    }
	    if (b != null) {
	        e.setThatValue(b.getValue());
	        e.setThatUnit(b.getUnit());
	        e.setThatMeasurementType(b.getMeasurementType());
	    }
	    e.setOperation(op);
	    return e;
	}

	private void handleError(QuantityMeasurementEntity entity, String msg) {
	    entity.setError(true);
	    entity.setErrorMessage(msg);
	    log.error(msg);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Quantity convertDtoToModel(QuantityDTO dto) {
	    IMeasurable unit = resolveUnit(dto.getMeasurementType(), dto.getUnit());
	    return new Quantity<>(dto.getValue(), unit);
	}

	private IMeasurable resolveUnit(String measurementType, String unitName) {
	    if (measurementType == null || unitName == null) {
	        throw QuantityMeasurementException.invalidConversion("Measurement type and unit are required");
	    }

	    String mt = measurementType.trim();
	    String un = unitName.trim();

	    IMeasurable resolved = switch (mt) {
	        case "LengthUnit" -> LengthUnit.FEET.fromUnitName(un);
	        case "WeightUnit" -> WeightUnit.KILOGRAM.fromUnitName(un);
	        case "VolumeUnit" -> VolumeUnit.LITER.fromUnitName(un);
	        case "TemperatureUnit" -> TemperatureUnit.CELSIUS.fromUnitName(un);
	        default -> throw QuantityMeasurementException.invalidConversion("Unknown measurement type: " + measurementType);
	    };

	    if (resolved == null) {
	        throw QuantityMeasurementException.invalidConversion("Unit must be valid for the specified measurement type");
	    }

	    return resolved;
	}
}