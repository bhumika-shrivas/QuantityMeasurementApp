package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.*;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController    // = @Controller + @ResponseBody (returns JSON automatically)
@RequestMapping("/api/v1/quantities")  // base URL for all endpoints
@Tag(name = "Quantity Measurement API", description = "Endpoints for comparing, converting, and performing arithmetic on quantities")
public class QuantityMeasurementController{
	
	private static final Logger log = LoggerFactory.getLogger(QuantityMeasurementController.class);
	
	@Autowired  // Spring injects service
	private IQuantityMeasurementService service;
	
	// POST /api/v1/quantities/compare
	@PostMapping("/compare")
	@Operation(summary = "Compare two quantities")
	public ResponseEntity<QuantityMeasurementDTO> compareQuantities(
			@Valid @RequestBody QuantityInputDTO input) {
		log.info("POST/compare");
		QuantityMeasurementDTO result = service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
		return ResponseEntity.ok(result);
	}
	
	// POST /api/v1/quantities/convert
	@PostMapping("/convert")
	@Operation(summary = "Convert a quantity to a different unit")
	public ResponseEntity<QuantityMeasurementDTO> convertQuantity(
			@Valid @RequestBody QuantityInputDTO input) {
		String targetUnit = input.getThatQuantityDTO() != null ? input.getThatQuantityDTO().getUnit() : input.getTargetUnit();
		QuantityMeasurementDTO result = service.convert(input.getThisQuantityDTO(), targetUnit);
		return ResponseEntity.ok(result);	
	}
	
	// POST /api/v1/quantities/add
    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> addQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.add(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }
    
    // POST /api/v1/quantities/subtract
    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityMeasurementDTO> subtractQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.subtract(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }
	
    // POST /api/v1/quantities/divide
    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities — returns dimensionless ratio")
    public ResponseEntity<QuantityMeasurementDTO> divideQuantities(
            @Valid @RequestBody QuantityInputDTO input) {
        QuantityMeasurementDTO result = service.divide(
                input.getThisQuantityDTO(), input.getThatQuantityDTO());
        return ResponseEntity.ok(result);
    }
 
    // GET /api/v1/quantities/history/operation/ADD
    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get history by operation type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getByOperation(
            @PathVariable String operation) {
        return ResponseEntity.ok(service.getHistoryByOperation(operation.toUpperCase()));
    }
 
    // GET /api/v1/quantities/history/type/LengthUnit
    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get history by measurement type")
    public ResponseEntity<List<QuantityMeasurementDTO>> getByType(
            @PathVariable String measurementType) {
        return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
    }
 
    // GET /api/v1/quantities/history/errored
    @GetMapping("/history/errored")
    @Operation(summary = "Get all error records")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrors() {
        return ResponseEntity.ok(service.getErrorHistory());
    }
 
    // GET /api/v1/quantities/count/COMPARE
    @GetMapping("/count/{operation}")
    @Operation(summary = "Count successful operations by type")
    public ResponseEntity<Long> getCount(@PathVariable String operation) {
        return ResponseEntity.ok(service.getCountByOperation(operation.toUpperCase()));
    }
}