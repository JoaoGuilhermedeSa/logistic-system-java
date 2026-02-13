package com.logistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.LogisticSystemApplication;
import com.logistics.dto.RateUpdateRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LogisticSystemApplication.class)
@AutoConfigureMockMvc
@Transactional
class RateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllActiveRates() throws Exception {
        mockMvc.perform(get("/api/v1/rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldGetRateByType() throws Exception {
        mockMvc.perform(get("/api/v1/rates/{type}", "BOAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportationType").value("BOAT"))
                .andExpect(jsonPath("$.baseRatePerUnit").value(10.0))
                .andExpect(jsonPath("$.volumeMultiplier").value(1.5))
                .andExpect(jsonPath("$.sizeMultiplier").value(2.0));
    }

    @Test
    void shouldUpdateRate() throws Exception {
        RateUpdateRequest request = new RateUpdateRequest(
                new BigDecimal("15.00"), new BigDecimal("1.80"), new BigDecimal("2.50"));

        mockMvc.perform(put("/api/v1/rates/{type}", "BOAT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transportationType").value("BOAT"))
                .andExpect(jsonPath("$.baseRatePerUnit").value(15.0))
                .andExpect(jsonPath("$.volumeMultiplier").value(1.8))
                .andExpect(jsonPath("$.sizeMultiplier").value(2.5));
    }

    @Test
    void shouldGetRateHistoryAfterUpdate() throws Exception {
        RateUpdateRequest request = new RateUpdateRequest(
                new BigDecimal("15.00"), new BigDecimal("1.80"), new BigDecimal("2.50"));

        mockMvc.perform(put("/api/v1/rates/{type}", "BOAT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/rates/{type}/history", "BOAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].previousValue").value(10.0))
                .andExpect(jsonPath("$[0].newValue").value(15.0));
    }

    @Test
    void shouldRejectMissingRateFields() throws Exception {
        mockMvc.perform(put("/api/v1/rates/{type}", "BOAT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNegativeBaseRate() throws Exception {
        RateUpdateRequest request = new RateUpdateRequest(
                new BigDecimal("-5.00"), new BigDecimal("1.00"), new BigDecimal("1.00"));

        mockMvc.perform(put("/api/v1/rates/{type}", "BOAT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnEmptyHistoryForUnchangedRate() throws Exception {
        mockMvc.perform(get("/api/v1/rates/{type}/history", "TRUCK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
