package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.dto.*;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuantityMeasurementController.class)
@WithMockUser  // simulates a logged-in user so Security doesn't block
class QuantityMeasurementControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean IQuantityMeasurementService service;

    @Test
    void testCompareQuantities_Returns200() throws Exception {
        QuantityMeasurementDTO mockResult = new QuantityMeasurementDTO();
        mockResult.setResultValue("true");
        mockResult.setOperation("COMPARE");
        Mockito.when(service.compare(Mockito.any(), Mockito.any())).thenReturn(mockResult);

        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FEET", "LengthUnit", null),
            new QuantityDTO(12.0, "INCHES", "LengthUnit", null),
            null);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())   // adds CSRF token to POST request
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resultValue").value("true"))
            .andExpect(jsonPath("$.operation").value("COMPARE"));
    }

    @Test
    void testInvalidInput_Returns400() throws Exception {
        QuantityInputDTO input = new QuantityInputDTO(
            new QuantityDTO(1.0, "FOOT", "LengthUnit", null), null, null);

        mockMvc.perform(post("/api/v1/quantities/compare")
                .with(csrf())   // adds CSRF token
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400));
    }
}