package com.logistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.LogisticSystemApplication;
import com.logistics.dto.FreightQuoteRequest;
import com.logistics.dto.RateUpdateRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LogisticSystemApplication.class)
@AutoConfigureMockMvc
@Transactional
class FreightEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUseUpdatedRateForNewQuote() throws Exception {
        FreightQuoteRequest quoteRequest = new FreightQuoteRequest(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"));

        // Step 1: Create a quote with the original BOAT rate
        // price = 10 * (100 * 1.5) + (2000 * 2.0) = 1500 + 4000 = 5500.00
        mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quoteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calculatedPrice").value(5500.00))
                .andExpect(jsonPath("$.rateApplied.baseRatePerUnit").value(10.0));

        // Step 2: Update the BOAT rate
        RateUpdateRequest rateUpdate = new RateUpdateRequest(
                new BigDecimal("15.00"), new BigDecimal("2.00"), new BigDecimal("3.00"));

        mockMvc.perform(put("/api/v1/rates/{type}", "BOAT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rateUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseRatePerUnit").value(15.0));

        // Step 3: Create a new quote - should use the updated rate
        // price = 15 * (100 * 2.0) + (2000 * 3.0) = 3000 + 6000 = 9000.00
        mockMvc.perform(post("/api/v1/freight/quote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quoteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.calculatedPrice").value(9000.00))
                .andExpect(jsonPath("$.rateApplied.baseRatePerUnit").value(15.0))
                .andExpect(jsonPath("$.rateApplied.volumeMultiplier").value(2.0))
                .andExpect(jsonPath("$.rateApplied.sizeMultiplier").value(3.0));

        // Step 4: Verify rate history was created
        mockMvc.perform(get("/api/v1/rates/{type}/history", "BOAT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].previousValue").value(10.0))
                .andExpect(jsonPath("$[0].newValue").value(15.0));
    }
}
