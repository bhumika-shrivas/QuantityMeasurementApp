package com.app.quantitymeasurement.dto;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityMeasurementDTO {

    private Long id;
    private double thisValue;
    private String thisUnit;
    private String thisMeasurementType;
    private double thatValue;
    private String thatUnit;
    private String thatMeasurementType;
    private String operation;
    private String resultValue;
    private String resultUnit;
    private String errorMessage;
    private boolean error;
    private LocalDateTime createdAt;

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity e) {
        return QuantityMeasurementDTO.builder()
                .id(e.getId())
                .thisValue(e.getThisValue())
                .thisUnit(e.getThisUnit())
                .thisMeasurementType(e.getThisMeasurementType())
                .thatValue(e.getThatValue())
                .thatUnit(e.getThatUnit())
                .thatMeasurementType(e.getThatMeasurementType())
                .operation(e.getOperation())
                .resultValue(e.getResultValue())
                .resultUnit(e.getResultUnit())
                .errorMessage(e.getErrorMessage())
                .error(e.isError())
                .createdAt(e.getCreatedAt())
                .build();
    }

    public QuantityMeasurementEntity toEntity() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(this.thisValue);
        entity.setThisUnit(this.thisUnit);
        entity.setThisMeasurementType(this.thisMeasurementType);
        entity.setThatValue(this.thatValue);
        entity.setThatUnit(this.thatUnit);
        entity.setThatMeasurementType(this.thatMeasurementType);
        entity.setOperation(this.operation);
        entity.setResultValue(this.resultValue);
        entity.setResultUnit(this.resultUnit);
        entity.setErrorMessage(this.errorMessage);
        entity.setError(this.error);
        return entity;
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
