package com.cg.measurement.controller;

import com.cg.measurement.model.MeasurementRequest;
import com.cg.measurement.model.MeasurementResponse;
import com.cg.measurement.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measure")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping("/test")
    public String test() {
        return "Measurement Service Working";
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculate(@RequestBody MeasurementRequest request) {
        try {
            MeasurementResponse response = measurementService.calculate(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
