package com.QuantityMeasurementApp.service.impl;

import com.QuantityMeasurementApp.Quantity;
import com.QuantityMeasurementApp.IMeasurable;
import com.QuantityMeasurementApp.dto.QuantityDTO;
import com.QuantityMeasurementApp.dto.QuantityMapper;
import com.QuantityMeasurementApp.entity.QuantityMeasurementEntity;
import com.QuantityMeasurementApp.exception.QuantityMeasurementException;
import com.QuantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.QuantityMeasurementApp.repository.impl.QuantityMeasurementCacheRepository;
import com.QuantityMeasurementApp.service.IQuantityMeasurementService;
import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.WeightUnit;

import java.time.Instant;

/**
 * Concrete service implementation that converts DTOs -> domain, performs operations,
 * persists an operation record and returns DTO results.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl() {
        this.repository = QuantityMeasurementCacheRepository.getInstance();
    }

    @Override
    public QuantityDTO compare(QuantityDTO left, QuantityDTO right) {
        String opType = "COMPARE";
        QuantityMeasurementEntity entity = null;
        try {
            validateNotNull(left, right);
            Quantity<?> qLeft = QuantityMapper.toQuantity(left);
            Quantity<?> qRight = QuantityMapper.toQuantity(right);
            validateSameCategory(qLeft, qRight);

            double baseLeft = qLeft.getUnit().convertToBaseUnit(qLeft.getValue());
            double baseRight = qRight.getUnit().convertToBaseUnit(qRight.getValue());
            int cmp = Double.compare(baseLeft, baseRight);
            QuantityDTO result = new QuantityDTO(cmp, left.getUnit(), left.getMeasurementType());

            entity = buildEntity(left, right, opType, result.toString(), null);
            repository.saveMeasurement(entity);

            return result;
        } catch (RuntimeException ex) {
            String err = ex.getMessage();
            entity = (entity == null) ? buildEntity(left, right, opType, null, err) : entity;
            try { repository.saveMeasurement(entity); } catch (Exception ignore) {}
            throw (ex instanceof QuantityMeasurementException) ? ex : QuantityMeasurementException.invalidConversion(err);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO source, String targetUnit) {
        String opType = "CONVERT";
        QuantityMeasurementEntity entity = null;
        try {
            if (source == null) throw QuantityMeasurementException.invalidConversion("Source is null");
            Quantity<?> q = QuantityMapper.toQuantity(source);
            IMeasurable target = resolveTargetUnit(source.getMeasurementType(), targetUnit);
            if (target == null) throw QuantityMeasurementException.invalidConversion("Unknown target: " + targetUnit);

            @SuppressWarnings({"rawtypes","unchecked"})
            Quantity raw = (Quantity) q;
            Quantity converted = raw.convertTo(target);
            QuantityDTO result = QuantityMapper.fromQuantity(converted);

            entity = buildEntity(source, null, opType, result.toString(), null);
            repository.saveMeasurement(entity);

            return result;
        } catch (RuntimeException ex) {
            String err = ex.getMessage();
            entity = (entity == null) ? buildEntity(source, null, opType, null, err) : entity;
            try { repository.saveMeasurement(entity); } catch (Exception ignore) {}
            throw (ex instanceof QuantityMeasurementException) ? ex : QuantityMeasurementException.invalidConversion(err);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO a, QuantityDTO b) {
        String opType = "ADD";
        QuantityMeasurementEntity entity = null;
        try {
            validateNotNull(a, b);
            Quantity<?> qa = QuantityMapper.toQuantity(a);
            Quantity<?> qb = QuantityMapper.toQuantity(b);
            validateSameCategory(qa, qb);

            @SuppressWarnings({"rawtypes","unchecked"})
            Quantity rawA = (Quantity) qa;
            Quantity rawB = (Quantity) qb;
            Quantity resultQty = rawA.add(rawB);
            QuantityDTO result = QuantityMapper.fromQuantity(resultQty);

            entity = buildEntity(a, b, opType, result.toString(), null);
            repository.saveMeasurement(entity);

            return result;
        } catch (RuntimeException ex) {
            String err = ex.getMessage();
            entity = (entity == null) ? buildEntity(a, b, opType, null, err) : entity;
            try { repository.saveMeasurement(entity); } catch (Exception ignore) {}
            throw (ex instanceof QuantityMeasurementException) ? ex : QuantityMeasurementException.crossCategoryOperation(err);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO a, QuantityDTO b) {
        String opType = "SUBTRACT";
        QuantityMeasurementEntity entity = null;
        try {
            validateNotNull(a, b);
            Quantity<?> qa = QuantityMapper.toQuantity(a);
            Quantity<?> qb = QuantityMapper.toQuantity(b);
            validateSameCategory(qa, qb);

            @SuppressWarnings({"rawtypes","unchecked"})
            Quantity rawA = (Quantity) qa;
            Quantity rawB = (Quantity) qb;
            Quantity resultQty = rawA.subtract(rawB);
            QuantityDTO result = QuantityMapper.fromQuantity(resultQty);

            entity = buildEntity(a, b, opType, result.toString(), null);
            repository.saveMeasurement(entity);

            return result;
        } catch (RuntimeException ex) {
            String err = ex.getMessage();
            entity = (entity == null) ? buildEntity(a, b, opType, null, err) : entity;
            try { repository.saveMeasurement(entity); } catch (Exception ignore) {}
            throw (ex instanceof QuantityMeasurementException) ? ex : QuantityMeasurementException.crossCategoryOperation(err);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO a, QuantityDTO b) {
        String opType = "DIVIDE";
        QuantityMeasurementEntity entity = null;
        try {
            validateNotNull(a, b);
            Quantity<?> qa = QuantityMapper.toQuantity(a);
            Quantity<?> qb = QuantityMapper.toQuantity(b);
            validateSameCategory(qa, qb);

            @SuppressWarnings({"rawtypes","unchecked"})
            Quantity rawA = (Quantity) qa;
            Quantity rawB = (Quantity) qb;
            double numeric = rawA.divide(rawB);
            String unit = qa.getUnit().getUnitName() + "/" + qb.getUnit().getUnitName();
            QuantityDTO result = new QuantityDTO(numeric, unit, "RATIO");

            entity = buildEntity(a, b, opType, result.toString(), null);
            repository.saveMeasurement(entity);

            return result;
        } catch (RuntimeException ex) {
            String err = ex.getMessage();
            entity = (entity == null) ? buildEntity(a, b, opType, null, err) : entity;
            try { repository.saveMeasurement(entity); } catch (Exception ignore) {}
            if (ex instanceof ArithmeticException) throw QuantityMeasurementException.arithmeticError(err);
            throw (ex instanceof QuantityMeasurementException) ? ex : QuantityMeasurementException.crossCategoryOperation(err);
        }
    }

    // -----------------
    // Helpers
    // -----------------

    private void validateNotNull(QuantityDTO a, QuantityDTO b) {
        if (a == null || b == null) throw QuantityMeasurementException.invalidConversion("Null operand");
    }

    private void validateSameCategory(Quantity<?> a, Quantity<?> b) {
        if (!a.getUnit().getMeasurementType().equalsIgnoreCase(b.getUnit().getMeasurementType()))
            throw QuantityMeasurementException.crossCategoryOperation("Different measurement categories");
    }

    private QuantityMeasurementEntity buildEntity(QuantityDTO op1, QuantityDTO op2, String operationType, String result, String error) {
        String s1 = (op1 == null) ? null : op1.toString();
        String s2 = (op2 == null) ? null : op2.toString();
        return new QuantityMeasurementEntity(s1, s2, operationType, result, error, Instant.now());
    }

    private IMeasurable resolveTargetUnit(String measurementType, String targetUnitName) {
        if (measurementType == null || targetUnitName == null) return null;
        switch (measurementType.trim().toUpperCase()) {
            case "LENGTH": return LengthUnit.FEET.fromUnitName(targetUnitName);
            case "WEIGHT": return WeightUnit.KILOGRAM.fromUnitName(targetUnitName);
            case "VOLUME": return VolumeUnit.LITRE.fromUnitName(targetUnitName);
            case "TEMPERATURE": return TemperatureUnit.CELSIUS.fromUnitName(targetUnitName);
            default: return null;
        }
    }
}
