package com.app.quantitymeasurement.exception;
 
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
 
@RestControllerAdvice  // intercepts exceptions from all @RestControllers
public class GlobalExceptionHandler {
 
    // ① Handles @Valid validation failures (e.g. bad unit name)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
 
        // Collect all field error messages
        String message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(err -> err instanceof FieldError fe ? fe.getDefaultMessage() : err.getDefaultMessage())
                .findFirst().orElse("Validation failed");
 
        return buildError(HttpStatus.BAD_REQUEST, "Quantity Measurement Error",
                          message, request.getRequestURI());
    }
 
    // ② Handles our custom QuantityMeasurementException
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<Map<String, Object>> handleQuantityException(
            QuantityMeasurementException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Quantity Measurement Error",
                          ex.getMessage(), request.getRequestURI());
    }
 
    // ③ Catch-all for everything else (ArithmeticException, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                          ex.getMessage(), request.getRequestURI());
    }
 
    // ─── Helper: builds standard error map ───
    private ResponseEntity<Map<String, Object>> buildError(
            HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return new ResponseEntity<>(body, status);
    }
}
 
// JSON response format for errors:
// {
//   "timestamp": "2026-01-01T12:00:00",
//   "status": 400,
//   "error": "Quantity Measurement Error",
//   "message": "Unit must be valid for the specified measurement type",
//   "path": "/api/v1/quantities/add"
// }