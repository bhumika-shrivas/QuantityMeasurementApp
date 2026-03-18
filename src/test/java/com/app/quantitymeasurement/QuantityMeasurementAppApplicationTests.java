package com.app.quantitymeasurement;

import com.app.quantitymeasurement.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuantityMeasurementAppApplicationTests {

    @Autowired TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // verifies app starts without errors
    }

    @Test
    void testAddQuantities_FeetPlusInches() {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LengthUnit", null),
            new QuantityDTO(12.0, "INCHES", "LengthUnit", null),
            null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            "/api/v1/quantities/add", input, QuantityMeasurementDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResultValue()).isEqualTo("2.0");
    }

    @Test
    void testActuatorHealth() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/actuator/health", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }

    @Test
    void testCrossCategoryAdd_ReturnsError() {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET",     "LengthUnit", null),
            new QuantityDTO(1.0, "KILOGRAM", "WeightUnit", null),
            null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            "/api/v1/quantities/add", input, QuantityMeasurementDTO.class);

        assertThat(response.getBody().isError()).isTrue();
    }

    @Test
    void testCompareQuantities_EqualLengths() {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET",   "LengthUnit", null),
            new QuantityDTO(12.0, "INCHES", "LengthUnit", null),
            null);

        ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(
            "/api/v1/quantities/compare", input, QuantityMeasurementDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResultValue()).isEqualTo("true");
    }

    @Test
    void testGetHistoryByOperation() {
        ResponseEntity<String> response = restTemplate.getForEntity(
            "/api/v1/quantities/history/operation/COMPARE", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}