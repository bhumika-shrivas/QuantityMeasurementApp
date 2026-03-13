package com.app.quantitymeasurement.service.impl;

import java.time.Instant;

import com.app.quantitymeasurement.IMeasurable;
import com.app.quantitymeasurement.Quantity;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.dto.QuantityMapper;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.units.LengthUnit;
import com.app.quantitymeasurement.units.TemperatureUnit;
import com.app.quantitymeasurement.units.VolumeUnit;
import com.app.quantitymeasurement.units.WeightUnit;
import com.app.quantitymeasurement.util.ApplicationConfig;

/**
 * Concrete service implementation that converts DTOs -> domain, performs operations,
 * persists an operation record and returns DTO results.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    // UC16: Constructor Injection
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) throw new IllegalArgumentException("repository must not be null");
        this.repository = repository;
    }

    /**
     * No-arg constructor used for simple wiring. Reads repository.type from configuration
     * and instantiates the appropriate repository implementation. Falls back to the
     * in-memory cache repository using reflection to avoid a direct compile-time dependency.
     */
    public QuantityMeasurementServiceImpl() {
        String repoType = ApplicationConfig.getProperty("repository.type");
        if (repoType != null && repoType.trim().equalsIgnoreCase("database")) {
            this.repository = new com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository();
        } else {
            // fallback to cache repository via reflection
            IQuantityMeasurementRepository r = null;
            try {
                Class<?> clazz = Class.forName("com.app.quantitymeasurement.repository.impl.QuantityMeasurementCacheRepository");
                java.lang.reflect.Method m = clazz.getMethod("getInstance");
                Object inst = m.invoke(null);
                if (inst instanceof IQuantityMeasurementRepository) {
                    r = (IQuantityMeasurementRepository) inst;
                }
            } catch (Exception ignore) {
                // ignore and r remains null
            }
            if (r == null) {
                // last resort, instantiate database repo
                r = new com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository();
            }
            this.repository = r;
        }
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