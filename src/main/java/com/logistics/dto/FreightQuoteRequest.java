package com.logistics.dto;

import com.logistics.model.TransportationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record FreightQuoteRequest(
        @NotNull(message = "Transportation type is required")
        TransportationType transportationType,

        @NotNull(message = "Volume is required")
        @Positive(message = "Volume must be positive")
        BigDecimal volume,

        @NotNull(message = "Size is required")
        @Positive(message = "Size must be positive")
        BigDecimal size
) {
}
