package com.logistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.LogisticSystemApplication;
import com.logistics.dto.FreightQuoteRequest;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LogisticSystemApplication.class)
@AutoConfigureMockMvc
@Transactional
class FreightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateQuote() throws Exception {
        FreightQuoteRequest request = new FreightQuoteRequest(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"));

        // price = 10 * (100 * 1.5) + (2000 * 2.0) = 1500 + 4000 = 5500.00
        mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transportationType").value("BOAT"))
                .andExpect(jsonPath("$.volume").value(100))
                .andExpect(jsonPath("$.size").value(2000))
                .andExpect(jsonPath("$.calculatedPrice").value(5500.00))
                .andExpect(jsonPath("$.rateApplied.baseRatePerUnit").value(10.0))
                .andExpect(jsonPath("$.rateApplied.volumeMultiplier").value(1.5))
                .andExpect(jsonPath("$.rateApplied.sizeMultiplier").value(2.0))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void shouldGetExistingQuote() throws Exception {
        FreightQuoteRequest request = new FreightQuoteRequest(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"));

        String response = mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long quoteId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/v1/freight/quote/{id}", quoteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(quoteId))
                .andExpect(jsonPath("$.transportationType").value("BOAT"))
                .andExpect(jsonPath("$.calculatedPrice").value(5500.00));
    }

    @Test
    void shouldReturn404ForNonExistentQuote() throws Exception {
        mockMvc.perform(get("/api/v1/freight/quote/{id}", 99999))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCompareAllTransportTypes() throws Exception {
        FreightQuoteRequest request = new FreightQuoteRequest(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"));

        mockMvc.perform(post("/api/v1/freight/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.volume").value(100))
                .andExpect(jsonPath("$.size").value(2000))
                .andExpect(jsonPath("$.quotes").isArray())
                .andExpect(jsonPath("$.quotes.length()").value(3));
    }

    @Test
    void shouldRejectMissingFields() throws Exception {
        mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectNegativeVolume() throws Exception {
        FreightQuoteRequest request = new FreightQuoteRequest(
                TransportationType.BOAT, new BigDecimal("-10"), new BigDecimal("2000"));

        mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
